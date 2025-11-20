@file:Suppress("MissingPermission")

package gorosheg.pulsiq.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import gorosheg.pulsiq.bluetooth.BluetoothRepositoryImpl.Companion.cccdUUID
import gorosheg.pulsiq.bluetooth.BluetoothRepositoryImpl.Companion.heartRateMeasurementUUID
import gorosheg.pulsiq.bluetooth.BluetoothRepositoryImpl.Companion.heartRateServiceUUID
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

internal sealed class HeartRateConnectionState {
    data class HeartRate(val pulse: Int) : HeartRateConnectionState()
    data class IsConnected(val isConnected: Boolean) : HeartRateConnectionState()
    object Disconnected : HeartRateConnectionState()
}

class BluetoothConnector(private val context: Context) {

    private var gatt: BluetoothGatt? = null

    internal fun safeCloseGatt() {
        try {
            gatt?.disconnect()
            gatt?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        gatt = null
    }

    internal fun connect(device: BluetoothDevice): Flow<HeartRateConnectionState> = callbackFlow {
        gatt = device.connectGatt(
            context,
            false,
            object : BluetoothGattCallback() {

                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int
                ) {
                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED -> {
                            if (status == BluetoothGatt.GATT_SUCCESS) {
                                gatt.discoverServices()
                            } else {
                                trySend(HeartRateConnectionState.IsConnected(false))
                                trySend(HeartRateConnectionState.Disconnected)
                            }
                        }

                        BluetoothProfile.STATE_DISCONNECTED -> {
                            trySend(HeartRateConnectionState.IsConnected(false))
                            trySend(HeartRateConnectionState.Disconnected)
                        }
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        trySend(HeartRateConnectionState.IsConnected(false))
                        return
                    }

                    val characteristic = gatt
                        .getService(heartRateServiceUUID)
                        ?.getCharacteristic(heartRateMeasurementUUID)
                        ?: run {
                            trySend(HeartRateConnectionState.IsConnected(false))
                            return
                        }

                    gatt.enableNotifications(characteristic)
                    trySend(HeartRateConnectionState.IsConnected(true))
                }

                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    value: ByteArray
                ) {
                    trySend(HeartRateConnectionState.HeartRate(value.parseHeartRate()))
                }

                @Suppress("DEPRECATION")
                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic
                ) {
                    val bytes = characteristic.value ?: return
                    trySend(HeartRateConnectionState.HeartRate(bytes.parseHeartRate()))
                }
            }
        )
        awaitClose { Unit }
    }.distinctUntilChanged()

    @Suppress("DEPRECATION")
    private fun BluetoothGatt.enableNotifications(characteristic: BluetoothGattCharacteristic) {
        val supportsNotify = (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
        val supportsIndicate = (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
        if (!supportsNotify && !supportsIndicate) return

        setCharacteristicNotification(characteristic, true)

        val cccd = characteristic.getDescriptor(cccdUUID) ?: return
        val payload =
            if (supportsIndicate) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            else BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            writeDescriptor(cccd, payload)
        } else {
            cccd.value = payload
            writeDescriptor(cccd)
        }
    }

    private fun ByteArray.parseHeartRate(): Int {
        if (isEmpty()) return 0
        val flags = this[0].toInt()
        val isUint16 = (flags and 0x01) != 0

        return if (!isUint16) {
            getOrNull(1)?.toInt()?.and(0xFF) ?: 0
        } else {
            val b0 = getOrNull(1)?.toInt()?.and(0xFF) ?: 0
            val b1 = getOrNull(2)?.toInt()?.and(0xFF) ?: 0
            (b1 shl 8) or b0
        }
    }
}
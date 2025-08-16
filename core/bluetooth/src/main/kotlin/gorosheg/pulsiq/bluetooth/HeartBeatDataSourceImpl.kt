@file:Suppress(
    "MissingPermission",
    "DEPRECATION",
)

package gorosheg.pulsiq.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class HeartBeatDataSourceImpl(
    private val context: Context
) : HeartBeatDataSource {

    private val _heartRateFlow = MutableStateFlow(0)
    override val heartRateFlow: StateFlow<Int> = _heartRateFlow.asStateFlow()

    private val bluetoothManager by lazy { context.getSystemService(BluetoothManager::class.java) }
    private val bluetoothAdapter get() = bluetoothManager?.adapter
    private val scanner get() = bluetoothAdapter?.bluetoothLeScanner

    private val heartRateServiceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    private val heartRateMeasurementUUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
    private val cccdUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    private var gatt: BluetoothGatt? = null

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            scanner?.stopScan(this)
            gatt = result.device.connectGatt(context, false, gattCallback)
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val char = gatt.getService(heartRateServiceUUID)
                ?.getCharacteristic(heartRateMeasurementUUID) ?: return

            gatt.setCharacteristicNotification(char, true)
            val descriptor = char.getDescriptor(cccdUUID)
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }

        @Deprecated("Deprecated in API 33+")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            processHeartRate(characteristic.value)
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            processHeartRate(value)
        }
    }

    override fun startScan() {
        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(heartRateServiceUUID))
            .build()
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    override fun disconnect() {
        gatt?.close()
        gatt = null
    }

    private fun processHeartRate(value: ByteArray) {
        val flag = value[0].toInt()
        val bpm = if (flag and 0x01 == 0) value[1].toInt() and 0xFF
        else (value[1].toInt() and 0xFF) or ((value[2].toInt() and 0xFF) shl 8)
        _heartRateFlow.value = bpm
    }
}

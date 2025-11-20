package gorosheg.pulsiq.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import androidx.annotation.RequiresPermission
import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

internal sealed class BluetoothScannerState {
    data class OnDeviceReceived(val device: DomainBluetoothDevice) : BluetoothScannerState()
    data class OnCallbackTypeMatchLost(val device: BluetoothDevice) : BluetoothScannerState()
    data class OnListDeviceReceived(val deviceList: List<DomainBluetoothDevice>) : BluetoothScannerState()
}

class BluetoothScanner(private val scanner: BluetoothLeScanner?) {

    private var scanCallback: ScanCallback? = null

    internal fun initScanCallback(): Flow<BluetoothScannerState> = callbackFlow {
        scanCallback = object : ScanCallback() {

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device: BluetoothDevice = result.device ?: return

                if (callbackType == ScanSettings.CALLBACK_TYPE_MATCH_LOST) {
                    trySend(BluetoothScannerState.OnCallbackTypeMatchLost(device))
                    return
                }
                trySend(BluetoothScannerState.OnDeviceReceived(device.convertToBleDevice(result)))
            }

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                val updates: List<DomainBluetoothDevice> = results.map { result ->
                    val device: BluetoothDevice = result.device ?: return
                    device.convertToBleDevice(result)
                }
                trySend(BluetoothScannerState.OnListDeviceReceived(updates))
            }

            override fun onScanFailed(errorCode: Int) {}
        }
        awaitClose {
            scanCallback = null
        }
    }.distinctUntilChanged()

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    internal fun startScan(filter: ScanFilter, settings: ScanSettings) {
        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    internal fun stopScan() {
        scanner?.stopScan(scanCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun BluetoothDevice.convertToBleDevice(
        result: ScanResult
    ): DomainBluetoothDevice {
        return DomainBluetoothDevice(
            rssi = result.rssi,
            name = name ?: "Unknown",
            device = this,
            isConnectable = result.isConnectable
        )
    }
}
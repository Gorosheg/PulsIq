@file:Suppress("MissingPermission")
@file:OptIn(FlowPreview::class)

package gorosheg.pulsiq.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

internal class HeartBeatDataSourceImpl(
    private val context: Context,
    private val scanner: BluetoothLeScanner?,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : HeartBeatDataSource {

    private var gatt: BluetoothGatt? = null
    private var lastDevice: BluetoothDevice? = null
    private var isUserDisconnected = false
    private var reconnectionJob: Job? = null

    private val heartRateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(type: Int, result: ScanResult) {
            scanner?.stopScan(this)
            isUserDisconnected = false
            lastDevice = result.device

            reconnectionJob?.cancel()
            reconnectionJob = null
            connectToGatt()
        }
    }

    override fun subscribeHeartRateFlow(): Flow<Int> {
        return heartRateFlow
            .debounce(1000L)
            .distinctUntilChanged()
    }

    override fun startScan() {
        isUserDisconnected = false

        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(heartRateServiceUUID))
            .build()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    override fun disconnect() {
        isUserDisconnected = true
        reconnectionJob?.cancel()
        reconnectionJob = null

        scanner?.stopScan(scanCallback)
        safeCloseGatt()
    }

    private fun connectToGatt() {
        scope.launch(Dispatchers.Main.immediate) {
            safeCloseGatt()
            lastDevice ?: return@launch

            gatt = lastDevice?.connectGatt(
                context,
                false,
                onPulseChanged(
                    callback = { pulse -> heartRateFlow.value = pulse },
                    onDisconnected = ::startReconnectionAttempts
                )
            )
        }
    }

    private fun startReconnectionAttempts() {
        if (isUserDisconnected) return

        reconnectionJob?.cancel()
        reconnectionJob = scope.launch {
            delay(RECONNECT_DELAY_MS)
            connectToGatt()
        }
    }

    private fun safeCloseGatt() {
        try {
            gatt?.disconnect()
            gatt?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        gatt = null
    }

    companion object {

        internal val heartRateMeasurementUUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        internal val cccdUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        internal val heartRateServiceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")

        private const val RECONNECT_DELAY_MS = 1000L
    }
}

package gorosheg.pulsiq.device_connection.presentation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.bluetooth.model.BleDevice
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionEffect
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionUiStateMapper
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBleDevice
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class DeviceConnectionViewModel(
    private val heartBeatDataSource: HeartBeatDataSource,
) : BaseViewModel<DeviceConnectionState, DeviceConnectionUiState, DeviceConnectionEffect>(
    initState = DeviceConnectionState(),
    uiStateMapper = DeviceConnectionUiStateMapper()
) {

    init {
        viewModelScope.launch {
            heartBeatDataSource.subscribeAvailableDevicesFlow()
                .onEach {
                    state { copy(devices = it.toUi()) }
                }
                .launchIn(viewModelScope)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        heartBeatDataSource.startScan()
        state { copy(isScanning = true) }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        heartBeatDataSource.stopScan()
        state { copy(isScanning = false) }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onDeviceClick(address: String) {
        state { copy(connectingAddress = address) }
        viewModelScope.launch {
            val result: Result<Unit> = heartBeatDataSource.connect(address)
            if (result.isSuccess){
                state { copy(connectedAddress = address) }
            } else {
                state { copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        heartBeatDataSource.disconnect()
    }

    private fun List<BleDevice>.toUi(): List<UiBleDevice> {
        return map {
            UiBleDevice(
                name = it.name,
                address = it.address,
            )
        }
    }
}
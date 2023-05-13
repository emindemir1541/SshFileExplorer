package com.emindev.sshfileexplorer.main.data.sshrepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
import com.emindev.sshfileexplorer.main.common.util.SSHChannel
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceSortType.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceViewModel(private val dao: DeviceDao) : ViewModel() {

    private val _sortType = MutableStateFlow(DATE)
    private val _devices = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            DATE -> dao.getDeviceOrderByLastJoin()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(DeviceState())
    val state = combine(_state, _sortType, _devices) { state, sortType, devices ->
        state.copy(
            devices = devices,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DeviceState())

    fun onEvent(event: DeviceEvent) {
        when (event) {
            is DeviceEvent.DeleteDevice -> {
                viewModelScope.launch {
                    dao.deleteDevice(event.device)
                }
            }
            DeviceEvent.HideDialog -> _state.update { it.copy(isAddingDevice = false) }
            DeviceEvent.SaveDevice -> {
                val host = state.value.host
                val port = if (state.value.port.isNotBlank()) state.value.port.toInt() else 0
                val user = state.value.user
                val password = state.value.password
                val lastJoin = DateUtil.currentTime

                if (host.isBlank() || port == 0 || user.isBlank() || password.isBlank()) {
                    return
                }
                val device = Device(host, port, user, password, lastJoin)
                viewModelScope.launch {
                    dao.insertDevice(device)

                }
                _state.update { it.copy(isAddingDevice = false, host = "", port = "", user = "", password = "", lastJoin = 0) }
            }
            is DeviceEvent.SetHost -> {
                _state.update {
                    it.copy(host = event.hostName)
                }
            }
            is DeviceEvent.SetLastJoinDate -> {
                _state.update {
                    it.copy(lastJoin = event.lastJoin)
                }
            }
            is DeviceEvent.SetPassword -> {
                _state.update {
                    it.copy(password = event.password)
                }
            }
            is DeviceEvent.SetPort -> {
                _state.update {
                    it.copy(port = event.port)
                }
            }
            is DeviceEvent.SetUser -> {
                _state.update {
                    it.copy(user = event.user)
                }
            }
            DeviceEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingDevice = true)
                }
            }
            is DeviceEvent.SortDevicesByDate -> {
                _sortType.value = event.sortType
            }
            DeviceEvent.Connect -> {

                _state.update { it.copy(isConnected = true) }
            }
            DeviceEvent.Disconnect -> {
                SSHChannel.disconnect()
                _state.update { it.copy(isConnected = false) }
            }
        }
    }
}
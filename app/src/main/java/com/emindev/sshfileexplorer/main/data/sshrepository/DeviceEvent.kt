package com.emindev.sshfileexplorer.main.data.sshrepository

sealed interface DeviceEvent {
    object SaveDevice:DeviceEvent
    data class SetHost(val hostName:String):DeviceEvent
    data class SetPort(val port:String):DeviceEvent
    data class SetUser(val user:String):DeviceEvent
    data class SetPassword(val password: String):DeviceEvent
    data class SetLastJoinDate(val lastJoin:Long):DeviceEvent
    object ShowDialog:DeviceEvent
    object HideDialog:DeviceEvent
    object Connect:DeviceEvent
    object Disconnect:DeviceEvent
    data class SortDevicesByDate(val sortType: DeviceSortType):DeviceEvent
    data class DeleteDevice(val device:Device):DeviceEvent
}
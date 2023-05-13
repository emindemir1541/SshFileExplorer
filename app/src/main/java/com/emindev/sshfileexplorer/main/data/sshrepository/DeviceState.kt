package com.emindev.sshfileexplorer.main.data.sshrepository

data class DeviceState (
    val devices:List<Device> = emptyList(),
    val host:String ="",
    val port:String ="" ,
    val user:String = "",
    val password:String = "",
    val lastJoin:Long = 0,
    val isAddingDevice:Boolean = false,
    val isConnected:Boolean = false,
    val sortType: DeviceSortType =DeviceSortType.DATE
){
    fun toDevice(): Device {
        return Device(host,port.toInt(),user,password,lastJoin)
    }
}

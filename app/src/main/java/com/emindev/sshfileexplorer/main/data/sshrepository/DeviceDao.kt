package com.emindev.sshfileexplorer.main.data.sshrepository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Upsert
    suspend fun insertDevice(device: Device)

    @Delete
    suspend fun deleteDevice(device: Device)

    @Query("select * from device order by lastJoin ASC")
    fun getDeviceOrderByLastJoin():Flow<List<Device>>

/*    @Query("select * from device where host=:host and port=:port and user=:user and password=:password limit")
    fun getDeviceList(host:String,port:Int,user:String,password: String)*/
}
package com.emindev.sshfileexplorer.main.data.sshrepository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
    val host:String,
    val port:Int,
    val user:String,
    val password:String,
    val lastJoin:Long,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
)
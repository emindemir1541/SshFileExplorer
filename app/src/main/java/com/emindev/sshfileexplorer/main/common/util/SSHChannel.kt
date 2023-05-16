package com.emindev.sshfileexplorer.main.common.util

import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.data.sshrepository.Device
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

object SSHChannel {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var session: Session

    val connection = flow<Boolean> {
        while (true) {
            emit(session.isConnected)
            delay(1000)
        }
    }

    fun connect(device: Device, situation: (Resource<String>) -> Unit) {
        situation.invoke(Resource.Loading())
        coroutineScope.launch {
            try {

                session = JSch().getSession(device.user, device.host, device.port)
                session.setPassword(device.password)
                session.setConfig("StrictHostKeyChecking", "no")
                session.connect()
                situation.invoke(Resource.Success(null))
            } catch (e: Exception) {
                situation.invoke(Resource.Error(e.localizedMessage))
            }
        }
    }

    fun command(command: String, output: (Resource<String>) -> Unit) {
        output.invoke(Resource.Loading())
        coroutineScope.launch {
            try {

                val channel = session.openChannel("exec") as ChannelExec
                val outputStream = ByteArrayOutputStream()
                channel.outputStream = outputStream
                channel.setCommand(command)
                channel.connect()

                while (!channel.isClosed) {
                    Thread.sleep(1000)
                }

                channel.disconnect()

                output.invoke(Resource.Success(outputStream.toString()))
            } catch (e: Exception) {
                output.invoke(Resource.Error(e.localizedMessage))
            }
        }
    }

    fun disconnect() = coroutineScope.launch {
        try {

            if (session.isConnected)
                session.disconnect()
        } catch (_: Exception) {

        }

    }
}
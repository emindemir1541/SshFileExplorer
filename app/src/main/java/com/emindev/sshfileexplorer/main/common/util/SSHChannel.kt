package com.emindev.sshfileexplorer.main.common.util

import android.content.Context
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.data.sshrepository.Device
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
object SSHChannel {

    private var session: Session? = null

    val connection = flow<Boolean> {
        while (true) {
            emit(session?.isConnected ?: false)
            delay(1000)
        }
    }


    fun connect(context: Context, device: Device, situation: (Resource<String>) -> Unit) {
        situation.invoke(Resource.Loading())
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable.localizedMessage != ErrorUtil.currentStateThreadError && throwable.localizedMessage != null) {
                if (throwable.localizedMessage!!.contains(ErrorUtil.networkIsUnReachable)) {
                    if (!Helper.isOnline(context))
                        situation.invoke(Resource.Error(context.getString(R.string.is_online_error)))
                    else
                        situation.invoke(Resource.Error(context.getString(R.string.is_online_error_unknown_reason)))
                }
                else {
                    situation.invoke(Resource.Error(throwable.localizedMessage!!))
                }
            }

        }

        CoroutineScope(Dispatchers.IO + errorHandler).launch {
             launch {
                session = JSch().getSession(device.user, device.host, device.port)
                session!!.setPassword(device.password)
                session!!.setConfig("StrictHostKeyChecking", "no")
                session!!.connect()
                situation.invoke(Resource.Success(null))
            }
        }
    }

    fun command(command: String) = CoroutineScope(Dispatchers.IO).async {

        val channel = session!!.openChannel("exec") as ChannelExec
        val outputStream = ByteArrayOutputStream()
        channel.outputStream = outputStream
        channel.setCommand(command)
        channel.connect()

        while (!channel.isClosed) {
            Thread.sleep(1000)
        }

        channel.disconnect()

        outputStream.toString()
    }

    fun getFile(serverPath: String, localPath:String)=CoroutineScope(Dispatchers.IO).async {
        val channel = session!!.openChannel("sftp") as ChannelSftp
        channel.connect()
        channel.get(serverPath,localPath)
        channel.disconnect()
        "Success"
    }

    fun disconnect() = CoroutineScope(Dispatchers.IO).launch {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (session?.isConnected == true)
                    session?.disconnect()
            } catch (_: Exception) {

            }
        }

    }
}
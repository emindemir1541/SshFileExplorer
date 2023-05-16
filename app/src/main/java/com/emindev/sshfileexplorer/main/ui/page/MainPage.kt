package com.emindev.sshfileexplorer.main.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil
import com.emindev.sshfileexplorer.main.common.util.SSHChannel
import com.emindev.sshfileexplorer.main.data.sshrepository.Device
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceEvent
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceState

@Composable
fun MainPage(state: DeviceState, onEvent: (DeviceEvent) -> Unit, explorerPage: MutableState<Boolean>) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            onEvent(DeviceEvent.ShowDialog)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = state.host, onValueChange = { onEvent(DeviceEvent.SetHost(it)) }, placeholder = { Text(text = stringResource(id = R.string.host)) })
                OutlinedTextField(value = state.port, onValueChange = { onEvent(DeviceEvent.SetPort(it)) }, placeholder = { Text(text = stringResource(id = R.string.port)) })
                OutlinedTextField(value = state.user, onValueChange = { onEvent(DeviceEvent.SetUser(it)) }, placeholder = { Text(text = stringResource(id = R.string.username)) })
                OutlinedTextField(value = state.password, onValueChange = { onEvent(DeviceEvent.SetPassword(it)) }, placeholder = { Text(text = stringResource(id = R.string.password)) })
                Button(onClick = {

                    connect(state.toDevice(), onEvent, explorerPage)

                }) {
                    Text(text = stringResource(id = R.string.connect))
                }
            }
        }
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(text = stringResource(id = R.string.history))



            }
        }

        items(state.devices.reversed()) { device ->
            DeviceRow(device = device) {
                onEvent(DeviceEvent.SetHost(device.host))
                onEvent(DeviceEvent.SetPort(device.port.toString()))
                onEvent(DeviceEvent.SetUser(device.user))
                onEvent(DeviceEvent.SetPassword(device.password))
                onEvent(DeviceEvent.SetLastJoinDate(DateUtil.currentTime))

                connect(device, onEvent, explorerPage)
            }
        }

    }

}


@Composable
private fun DeviceRow(device: Device, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = DateUtil.convertToString(device.lastJoin))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = stringResource(id = R.string.host) + ": " + device.host)
            Text(text = stringResource(id = R.string.port) + ": " + device.port)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = stringResource(id = R.string.username) + ": " + device.user)
            Text(text = stringResource(id = R.string.password) + ": " + device.password)
        }
    }

}

fun connect(device: Device, onEvent: (DeviceEvent) -> Unit, explorerPage: MutableState<Boolean>) {


    SSHChannel.connect(device) { situation ->
        when (situation) {
            is Resource.Error -> addLog("Connection Error:",situation.message)
            is Resource.Loading -> {}
            is Resource.Success -> {
                SSHChannel.command("ls") { command ->
                    when (command) {
                        is Resource.Error -> addLog("Command Error:" ,command.message)
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onEvent(DeviceEvent.Connect)
                            onEvent(DeviceEvent.SaveDevice)
                            onEvent(DeviceEvent.HideDialog)
                            explorerPage.value = true
                        }
                    }
                }
            }
        }
    }


    onEvent(DeviceEvent.SaveDevice)
}


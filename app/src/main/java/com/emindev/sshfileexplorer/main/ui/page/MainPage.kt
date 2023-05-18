package com.emindev.sshfileexplorer.main.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
import com.emindev.sshfileexplorer.main.common.constant.Page
import com.emindev.sshfileexplorer.main.common.util.SSHChannel
import com.emindev.sshfileexplorer.main.data.sshrepository.Device
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceEvent
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceState
import com.emindev.sshfileexplorer.main.ui.component.LoadingDialog

@Composable
fun MainPage(navController: NavController,state: DeviceState, onEvent: (DeviceEvent) -> Unit) {

    val loadingDialog = remember { mutableStateOf(false) }

    LoadingDialog(show = loadingDialog)

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

                    connect(state.toDevice(), onEvent, navController, loadingDialog)

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

                connect(device, onEvent, navController, loadingDialog)
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

fun connect(device: Device, onEvent: (DeviceEvent) -> Unit, navController: NavController, loadingDialog: MutableState<Boolean>) {

    SSHChannel.connect(device) { situation ->
        when (situation) {
            is Resource.Error -> {
                addLog("Connection Error:", situation.message)
                loadingDialog.value = false
            }
            is Resource.Loading -> {
                loadingDialog.value = true
            }
            is Resource.Success -> {
                onEvent(DeviceEvent.Connect)
                onEvent(DeviceEvent.SaveDevice)
                onEvent(DeviceEvent.HideDialog)
                loadingDialog.value = false
                navController.navigate(Page.Explorer.route)
            }
        }
    }

    onEvent(DeviceEvent.SaveDevice)
}


package com.emindev.sshfileexplorer.main.ui.page

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.main.common.constant.FileType
import com.emindev.sshfileexplorer.main.common.model.ExplorerViewModel
import com.emindev.sshfileexplorer.main.common.model.FileModel
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceEvent

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ExplorerPage(explorerPage: MutableState<Boolean>, viewModel: ExplorerViewModel,onEvent:(DeviceEvent)->Unit) {

    val context = LocalContext.current
    val backEnabled = remember { mutableStateOf(true) }
    val fileResource = viewModel.resource.collectAsState()
    val currentPathString = viewModel.currentPathString.collectAsState()
    val situation = remember { mutableStateOf<Resource<String>>(Resource.Success(null)) }
    val isOnline = Helper.isOnlineFlow(context).collectAsState(false)

    viewModel.openConnection()


    checkConnections(situation, isOnline, context)

    BackHandler(backEnabled.value) {
        if (currentPathString.value == StringHelper.delimiter) {
            viewModel.closeConnection()
            onEvent(DeviceEvent.Disconnect)
            explorerPage.value = false
           backEnabled.value = false
        }
        else {
            backEnabled.value = true
            viewModel.backPath()
        }

    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        item {
            Text(modifier = Modifier.padding(16.dp), text = stringResource(id = R.string.path) + ":    ${currentPathString.value}", fontWeight = FontWeight.Bold)
        }

        when (fileResource.value) {
            is Resource.Success -> {
                items(fileResource.value.data ?: emptyList()) { file ->
                    FileRow(file = file) {
                        if (file.fileType == FileType.FOLDER)
                            viewModel.nextPath(file.fileName)
                        else   {
                            // TODO: Download File
                        }
                    }
                }
            }
            is Resource.Loading -> {
                item {
                    LoadingView()
                }
            }
            is Resource.Error -> {
                item {
                    ErrorView(fileResource.value.message ?: stringResource(id = R.string.unknown_error))
                }
            }
        }


    }

}


@Composable
private fun FileRow(file: FileModel, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color.Gray)
        .clickable { onClick() }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Image(modifier = Modifier
            .size(100.dp)
            .padding(16.dp), painter = painterResource(id = file.imageSource), contentDescription = stringResource(id = file.imageDescriptionSource))
        Text(modifier = Modifier.padding(16.dp), text = file.fileName)
    }
}

@Composable
private fun LoadingView() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun ErrorView(error: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = error)
    }
}

private fun checkConnections(situation: MutableState<Resource<String>>, isOnline: State<Boolean>, context: Context) {
    if (!isOnline.value) {
        situation.value = Resource.Error(context.getString(R.string.ssh_connection_lost))
    }
}
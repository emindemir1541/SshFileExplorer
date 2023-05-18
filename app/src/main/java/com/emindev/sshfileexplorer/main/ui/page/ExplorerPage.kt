@file:OptIn(DelicateCoroutinesApi::class)

package com.emindev.sshfileexplorer.main.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.widget.Toast
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
import androidx.navigation.NavController
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.PathHelper
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.main.common.constant.FileType
import com.emindev.sshfileexplorer.main.common.model.DialogViewModel
import com.emindev.sshfileexplorer.main.common.model.ErrorDialogModel
import com.emindev.sshfileexplorer.main.common.model.ExplorerViewModel
import com.emindev.sshfileexplorer.main.common.model.FileModel
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil
import com.emindev.sshfileexplorer.main.ui.component.ErrorView
import com.emindev.sshfileexplorer.main.ui.component.LoadingView
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceEvent
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ExplorerPage(navController: NavController, viewModel: ExplorerViewModel, onEvent: (DeviceEvent) -> Unit, dialogViewModel: DialogViewModel) {

    val context = LocalContext.current
    val backEnabled = remember { mutableStateOf(true) }
    val fileResource = viewModel.resource.collectAsState()
    val currentPathString = viewModel.currentPathString.collectAsState()
    val isOnline = Helper.isOnlineFlow(context).collectAsState(true)

    viewModel.openConnection()

    fun exit() {
        viewModel.closeConnection()
        onEvent(DeviceEvent.Disconnect)
        navController.popBackStack()
    }

    if (!isOnline.value) {
        dialogViewModel.showErrorDialog(ErrorDialogModel(true, context.getString(R.string.internet_connection_lost), context.getString(R.string.is_online_error)))
        exit()
    }

    BackHandler(backEnabled.value) {
        if (currentPathString.value == StringHelper.delimiter) {
            exit()
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
                        else {
                            ExplorerUtil.downloadFile(currentPathString.value + file.fileName, PathHelper.downloadFolderPath) {
                                test = when (it) {
                                    is Resource.Error -> {
                                        dialogViewModel.showErrorDialog(ErrorDialogModel(true, context.getString(R.string.download_error), it.message ?: context.getString(R.string.unknown_error)))
                                    }
                                    is Resource.Loading -> {
                                        dialogViewModel.showLoadingDialog()
                                    }
                                    is Resource.Success -> {
                                        dialogViewModel.hideAllDialogs()
                                        GlobalScope.launch {
                                            withContext(Dispatchers.Main){
                                                Toast.makeText(context, context.getString(R.string.downloaded_succesfuly), Toast.LENGTH_LONG).show()
                                            }
                                        }

                                    }
                                }
                            }
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


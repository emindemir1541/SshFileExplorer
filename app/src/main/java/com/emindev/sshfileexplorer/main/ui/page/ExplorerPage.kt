package com.emindev.sshfileexplorer.main.ui.page

import android.annotation.SuppressLint
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
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.main.common.constant.DataSituation
import com.emindev.sshfileexplorer.main.common.constant.DataSituation.*
import com.emindev.sshfileexplorer.main.common.model.ExplorerViewModel
import com.emindev.sshfileexplorer.main.common.model.FileModel
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ExplorerPage(explorerPage: MutableState<Boolean>, viewModel: ExplorerViewModel) {

    val context = LocalContext.current
    val backEnabled = remember { mutableStateOf(true) }
    val filesInPath = remember { mutableStateOf(emptyList<FileModel>()) }
    val foldersInPath = remember { mutableStateOf(emptyList<FileModel>()) }
    val currentPathList = viewModel.currentPathList.collectAsState()
    val currentPathString = viewModel.currentPathString.collectAsState()
    val situation = remember { mutableStateOf(SUCCESS) }
    val isOnline = Helper.isOnlineFlow(context).collectAsState(false)

    checkConnections(situation,isOnline)

    ExplorerUtil.foldersInPath(currentPathString.value) { folders ->
        when (folders) {
            is Resource.Error -> {
                foldersInPath.value = emptyList()
                situation.value = ERROR
            }
            is Resource.Loading -> {
                situation.value = LOADING
            }
            is Resource.Success -> {
                foldersInPath.value = (folders.data ?: emptyList())
                situation.value = SUCCESS
            }
        }
    }

    ExplorerUtil.filesInPath(currentPathString.value) { files ->
        when (files) {
            is Resource.Error -> {
                filesInPath.value = emptyList()
                situation.value = ERROR
            }
            is Resource.Loading -> {
                situation.value = LOADING
            }
            is Resource.Success -> {
                filesInPath.value = files.data ?: emptyList()
                situation.value = SUCCESS
            }
        }
    }




    BackHandler(backEnabled.value) {
        if (currentPathString.value == StringHelper.delimiter) {
            explorerPage.value = false
            backEnabled.value = false
        }
        else {
            backEnabled.value = true
            viewModel.backPath()
        }

    }

    Box(modifier = Modifier) {
        test = currentPathList.value
    }
    Box(modifier = Modifier) {
        test = currentPathString.value
    }
    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        item {
            Text(modifier = Modifier.padding(16.dp), text = stringResource(id = R.string.path) + ":    ${currentPathString.value}", fontWeight = FontWeight.Bold)
        }
        when (situation.value) {
            SUCCESS -> {
                items(foldersInPath.value) { folder ->
                    FileRow(file = folder) {
                        viewModel.nextPath(folder.fileName)
                    }
                }

                items(filesInPath.value) { file ->
                    FileRow(file = file) {

                    }
                }
            }
            LOADING -> {
                item {
                    LoadingView()
                }
            }
            ERROR -> {
                item{
                    ErrorView("error")
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
private fun ErrorView(error:String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = error)
    }
}

private fun checkConnections(situation: MutableState<DataSituation>,isOnline:State<Boolean>){
    if (!isOnline.value){
        situation.value = ERROR
    }
}
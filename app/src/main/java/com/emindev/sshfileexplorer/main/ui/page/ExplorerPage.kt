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
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
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
    val loading = remember { mutableStateOf(false) }

    ExplorerUtil.foldersInPath(currentPathString.value) { folders ->
        when (folders) {
            is Resource.Error -> {
                foldersInPath.value = emptyList()
                loading.value = false
            }
            is Resource.Loading -> {
                loading.value = true
            }
            is Resource.Success -> {
                foldersInPath.value = (folders.data ?: emptyList())
                loading.value = false
            }
        }
    }

    ExplorerUtil.filesInPath(currentPathString.value) { files ->
        when (files) {
            is Resource.Error -> {
                filesInPath.value = emptyList()
                loading.value = false
            }
            is Resource.Loading -> {
                loading.value = true
            }
            is Resource.Success -> {
                filesInPath.value = files.data ?: emptyList()
                loading.value = false
            }
        }
    }




    BackHandler(backEnabled.value) {
         if (currentPathString.value == "/") {
             explorerPage.value = false
             backEnabled.value = false
             test = "it is not working"
         }
         else {
             backEnabled.value = true
             //viewModel.backPath()
             viewModel.backPath()
             test = "back is working"
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
        if (loading.value) {

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator( color = Color.Black)
                }
            }
        }
        else {
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
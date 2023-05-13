package com.emindev.sshfileexplorer.main.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.main.common.model.FileModel
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil

@Composable
fun ExplorerPage() {

    val filesInPath = remember { mutableStateOf(emptyList<FileModel>()) }
    val foldersInPath = remember { mutableStateOf(emptyList<FileModel>()) }
    val currentPath = remember {
        mutableStateOf("/")
    }

    ExplorerUtil.foldersInPath(currentPath.value) { folders ->
        when (folders) {
            is Resource.Error -> {
                foldersInPath.value = emptyList()
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                foldersInPath.value = (folders.data ?: emptyList())
            }
        }
    }

    ExplorerUtil.filesInPath(currentPath.value) { files ->
        when (files) {
            is Resource.Error -> {
                filesInPath.value = emptyList()
            }
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                filesInPath.value = files.data ?: emptyList()
            }
        }
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        item {
            Text(modifier = Modifier.padding(16.dp),text = stringResource(id = R.string.path) + ":    /", fontWeight = FontWeight.Bold)
        }
        items(foldersInPath.value){folder->
            FileRow(file = folder){
                currentPath.value=currentPath.value+folder.fileName+"/"
            }
        }
        
        items(filesInPath.value) { file ->
            FileRow(file = file){

            }
        }
    }
}

@Composable
private fun FileRow(file: FileModel,onClick:()->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color.Gray)
        .clickable { onClick() }
        , horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Image(modifier = Modifier
            .size(100.dp)
            .padding(16.dp),painter = painterResource(id = file.imageSource), contentDescription = stringResource(id = file.imageDescriptionSource))
        Text(modifier = Modifier.padding(16.dp),text = file.fileName)
    }
}
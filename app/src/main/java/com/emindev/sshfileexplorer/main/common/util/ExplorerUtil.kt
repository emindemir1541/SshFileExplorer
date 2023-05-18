package com.emindev.sshfileexplorer.main.common.util

import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.common.constant.SSHCommands
import com.emindev.sshfileexplorer.main.common.model.FileModel
import com.emindev.sshfileexplorer.main.data.sshrepository.Device
import kotlinx.coroutines.*

object ExplorerUtil {

    fun sourceInPath(path: String, resource: (Resource<List<FileModel>>) -> Unit) {
        resource.invoke(Resource.Loading())
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            resource.invoke(Resource.Error(throwable.localizedMessage))
        }
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            launch {
                val fileList = ArrayList<FileModel>()
                fileList.addAll(FileModel.folder(SSHChannel.command(SSHCommands.getFoldersList(path)).await()))
                fileList.addAll(FileModel.specifyFile(SSHChannel.command(SSHCommands.getFilesList(path)).await()))
                resource.invoke(Resource.Success(fileList))
            }
        }
    }

    fun downloadFile(serverPath: String, localPath: String, resource: (Resource<String>) -> Unit) {
        resource.invoke(Resource.Loading())
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            resource.invoke(Resource.Error(throwable.localizedMessage))
        }
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            launch {
                resource.invoke(Resource.Success(SSHChannel.getFile(serverPath, localPath).await()))
            }
        }
    }


}
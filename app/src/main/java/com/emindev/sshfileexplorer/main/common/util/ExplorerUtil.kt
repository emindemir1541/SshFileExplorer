package com.emindev.sshfileexplorer.main.common.util

import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.common.model.FileModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ExplorerUtil {

    fun sourceInPath(path: String, resource: (Resource<List<FileModel>>) -> Unit) {
        resource.invoke(Resource.Loading())
        CoroutineScope(Dispatchers.IO).launch {
            val fileList = ArrayList<FileModel>()
            try {
                val output = SSHChannel.command("ls -d $path*/")
                fileList.addAll(FileModel.folder( output))
                fileList.addAll(FileModel.specifyFile(SSHChannel.command("ls -ap $path | grep -v /").split("\n") ))
                resource.invoke(Resource.Success(fileList))
            } catch (e: Exception) {
                resource.invoke(Resource.Error(e.localizedMessage))
            }
        }


    }


}
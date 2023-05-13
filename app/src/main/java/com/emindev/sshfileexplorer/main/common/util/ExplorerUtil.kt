package com.emindev.sshfileexplorer.main.common.util

import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.common.model.FileModel

object ExplorerUtil {
    fun filesInPath(path: String,files:(Resource<List<FileModel>>)->Unit) {
        SSHChannel.command("ls -apF $path | grep -v /") { command ->
            when (command) {
                is Resource.Error -> {files.invoke(Resource.Error(command.message))}
                is Resource.Loading -> files.invoke(Resource.Loading())
                is Resource.Success -> files.invoke(Resource.Success(FileModel.specifyFile(command.data?.split("\n")?: emptyList())))
            }
        }
    }
    fun foldersInPath(path: String,files:(Resource<List<FileModel>>)->Unit){
        SSHChannel.command("ls -d $path*/" ) { command ->
            when (command) {
                is Resource.Error -> {files.invoke(Resource.Error(command.message))}
                is Resource.Loading -> files.invoke(Resource.Loading())
                is Resource.Success -> files.invoke(Resource.Success(FileModel.folder(command.data?.split("\n")?: emptyList())))
            }
        }
    }
}
package com.emindev.sshfileexplorer.main.common.constant

import com.emindev.sshfileexplorer.main.data.sshrepository.Device

object SSHCommands {
    fun getFoldersList(path:String) = "ls -d $path*/"
    fun getFilesList(path:String) = "ls -ap $path | grep -v /"
}
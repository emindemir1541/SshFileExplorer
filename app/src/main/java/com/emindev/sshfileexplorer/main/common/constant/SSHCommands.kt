package com.emindev.sshfileexplorer.main.common.constant

object SSHCommands {
    fun getFolders(path:String) = "ls -d $path*/"
    fun getFiles(path:String) = "ls -ap $path | grep -v /"
}
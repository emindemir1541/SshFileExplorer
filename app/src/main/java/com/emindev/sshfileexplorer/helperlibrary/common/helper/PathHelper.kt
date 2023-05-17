package com.emindev.sshfileexplorer.helperlibrary.common.helper

import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.main.common.constant.FileType
import com.emindev.sshfileexplorer.main.common.model.FileModel


object PathHelper {
    fun listToString(pathList: ArrayList<String>): String {
        var pathString = StringHelper.delimiter
        pathList.forEach { path ->
            pathString += path + StringHelper.delimiter
        }
        return pathString
    }

    fun pathToList() {

    }

    fun folderCommandToList(command: String): ArrayList<String> {
        val folderList = ArrayList<String>()

        command.split("\n").forEach { line ->
            val lineArray = line.split(StringHelper.delimiter).toMutableList()
            lineArray.removeAll(listOf("", null, " "))

            if (lineArray.isNotEmpty())
                folderList.add(lineArray[lineArray.size - 1])

        }

        return folderList

    }
}
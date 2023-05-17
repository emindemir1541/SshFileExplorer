package com.emindev.sshfileexplorer.main.common.model

import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.helperlibrary.common.helper.PathHelper
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper.clearBlankItems
import com.emindev.sshfileexplorer.main.common.constant.FileExtensions
import com.emindev.sshfileexplorer.main.common.constant.FileType

data class FileModel(val fileName: String, val fileType: FileType, val imageSource: Int, val imageDescriptionSource: Int) {

    companion object {

        fun specifyFile(command:String): List<FileModel> {
            val fileList = command.split("\n")
            val newFileList = ArrayList<FileModel>()

            fileList.forEach { fileName ->
                if (fileName.isNotBlank() && fileName != ".") {

                    val fileExtension = fileName.split(".").last()

                    if (FileExtensions.image.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.PICTURE, R.drawable.image, R.string.image))
                    }
                    else if (FileExtensions.video.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.VIDEO, R.drawable.video, R.string.video))
                    }
                    else if (FileExtensions.audio.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.AUDIO, R.drawable.music, R.string.audio))
                    }
                    else if (FileExtensions.archive.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.ARCHIVE, R.drawable.archive, R.string.archive))
                    }
                    else if (FileExtensions.text.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.TEXT, R.drawable.text, R.string.text))
                    }
                    else if (FileExtensions.program.contains(fileExtension)) {
                        newFileList.add(FileModel(fileName, FileType.PROGRAM, R.drawable.program, R.string.program))
                    }
                    else {
                        newFileList.add(FileModel(fileName, FileType.UNKNOWN, R.drawable.unknown, R.string.unknown))
                    }
                }
            }

            return newFileList
        }

        fun folder(folderListCommand: String): List<FileModel> {

            val pathList = ArrayList<FileModel>()

            PathHelper.folderCommandToList(folderListCommand).forEach { path ->
                pathList.add(FileModel(path, FileType.FOLDER, R.drawable.folder, R.string.folder))
            }

            return pathList
        }

    }

}
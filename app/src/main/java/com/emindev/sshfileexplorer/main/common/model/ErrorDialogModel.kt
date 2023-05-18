package com.emindev.sshfileexplorer.main.common.model

data class ErrorDialogModel(
    var isShowing:Boolean,
    val title:String,
    val message:String,
    )
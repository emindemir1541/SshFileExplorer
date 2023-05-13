package com.emindev.sshfileexplorer.main.common.model

sealed class Page(val name:String) {
    object Main:Page("Main")
    object Explorer:Page("Main")

}
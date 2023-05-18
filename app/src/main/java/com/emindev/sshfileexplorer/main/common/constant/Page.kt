package com.emindev.sshfileexplorer.main.common.constant



sealed class Page(val route: String) {
    object Main : Page("MAIN")
    object Explorer : Page("EXPLORER")
}

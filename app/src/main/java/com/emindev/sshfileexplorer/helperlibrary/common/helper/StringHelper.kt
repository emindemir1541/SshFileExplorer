package com.emindev.sshfileexplorer.helperlibrary.common.helper

object StringHelper {

    const val delimiter = "/"

    val String.cleanBlanks: String
        get() = this.replace(" ", "")

    val String.clearDelimiter: String
        get() = this.replace("/", "")

    fun ArrayList<String>.clearBlankItems(): ArrayList<String> {
        this.forEach {
            if (it.isBlank())
                this.remove(it)
        }
        return this
    }

    fun Float.clearZero(): String {
        val splitValue = this.toString().split(".")
        return if (splitValue[1] == "0") {
            splitValue[0]
        }
        else this.toString()
    }
}
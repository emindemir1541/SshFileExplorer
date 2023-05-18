package com.emindev.sshfileexplorer.helperlibrary.common.helper

object StringHelper {

    const val delimiter = "/"
    const val escapeSequence = "\n"
    const val dat = "."
    const val empty = ""
    const val space = " "

    val String.cleanBlanks: String
        get() = this.replace(space, empty)

    val String.clearDelimiter: String
        get() = this.replace(delimiter, empty)

    fun ArrayList<String>.clearBlankItems(): ArrayList<String> {
        this.forEach {
            if (it.isBlank())
                this.remove(it)
        }
        return this
    }

    fun Float.clearZero(): String {
        val splitValue = this.toString().split(dat)
        return if (splitValue[1] == "0") {
            splitValue[0]
        }
        else this.toString()
    }
}
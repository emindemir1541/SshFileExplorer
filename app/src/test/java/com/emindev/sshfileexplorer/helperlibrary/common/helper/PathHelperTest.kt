package com.emindev.sshfileexplorer.helperlibrary.common.helper

import org.junit.Test

class PathHelperTest() {

    private val pathTestCommand1 = "/bin/\n" +
            "/boot/\n" +
            "/cdrom/\n" +
            "/dev/\n" +
            "/etc/\n" +
            "/home/\n" +
            "/lib/\n" +
            "/lib32/\n" +
            "/lib64/\n" +
            "/libx32/\n"

    private val pathTestCommand2 = "/home/emin/"

    private val pathTestCommandEmpty = ""

    @Test
    fun command_to_list_test1() {

        try {
           val list =  PathHelper.folderCommandToList(pathTestCommand1)
            list.forEach {
                if (it.contains("/")||it.contains(" "))
                    assert(false){list}
            }
        }
        catch (e:Exception){
            assert(false)
        }

    }

    @Test
    fun command_to_list_test2() {
        try {
            val list =  PathHelper.folderCommandToList(pathTestCommand2)
            list.forEach {
                if (it !="emin")
                    assert(false){list}
            }
        }
        catch (e:Exception){
            assert(false)
        }
    }

    @Test
    fun command_to_list_empty_test() {
        try {
            val list =  PathHelper.folderCommandToList(pathTestCommandEmpty)
            list.forEach {
                    assert(false){list}
            }
        }
        catch (e:Exception){
            assert(false)
        }
    }
    @Test
    fun command_to_list_delimiter_test() {
        try {
            val list =  PathHelper.folderCommandToList("/")
            list.forEach {
                assert(false){list}
            }
        }
        catch (e:Exception){
            assert(false)
        }
    }


}
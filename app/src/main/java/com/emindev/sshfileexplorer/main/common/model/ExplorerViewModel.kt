package com.emindev.sshfileexplorer.main.common.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.sshfileexplorer.helperlibrary.common.helper.DateUtil
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ExplorerViewModel() : ViewModel() {


    private val _currentListArray = ArrayList<String>()
    private val _currentPathList = MutableStateFlow(ArrayList<String>())
    val currentPathList = _currentPathList.asStateFlow()

    private val _currentPathString = MutableStateFlow(StringHelper.delimiter)
    val currentPathString = _currentPathString.asStateFlow()

    fun nextPath(path: String) {
        val newArrayList = ArrayList<String>()
        newArrayList.add(path)
        newArrayList.addAll(_currentListArray)
        _currentListArray.add(path)
        _currentPathList.value = newArrayList
    }

    fun backPath() {
        if (_currentListArray.size != 0) {
            val newArrayList = ArrayList<String>()
            _currentListArray.removeLast()
            newArrayList.addAll(_currentListArray)
            _currentPathList.value = newArrayList
        }

    }

    init {
        currentPathString.combine(_currentPathList) { string, list ->
            var newString = StringHelper.delimiter
            _currentListArray.forEach {path->
               newString += path + StringHelper.delimiter
            }
            _currentPathString.value = newString
        }.launchIn(viewModelScope)
    }



}
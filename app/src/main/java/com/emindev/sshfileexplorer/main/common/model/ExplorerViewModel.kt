package com.emindev.sshfileexplorer.main.common.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.sshfileexplorer.helperlibrary.common.helper.PathHelper
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil
import com.emindev.sshfileexplorer.main.common.util.SSHChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExplorerViewModel() : ViewModel() {


    private val _currentListArray = ArrayList<String>()
    private val _currentPathList = MutableStateFlow(ArrayList<String>())
    val currentPathList = _currentPathList.asStateFlow()

    private val _currentPathString = MutableStateFlow(StringHelper.delimiter)
    val currentPathString = _currentPathString.asStateFlow()

    private val _resource = MutableStateFlow<Resource<List<FileModel>>>(Resource.Success(emptyList()))
    val resource = _resource.asStateFlow()

    fun openConnection() {
        _currentPathString.value = StringHelper.delimiter
        ExplorerUtil.sourceInPath(StringHelper.delimiter) { fileSource ->
            _resource.value = fileSource
        }
    }

    fun closeConnection() {
        _resource.value = Resource.Success(emptyList())
    }

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
            val newPath = PathHelper.listToString(_currentListArray)
            _currentPathString.value = newPath
            ExplorerUtil.sourceInPath(newPath) { fileSource ->
                _resource.value = fileSource
            }


        }.launchIn(viewModelScope)

    }


}
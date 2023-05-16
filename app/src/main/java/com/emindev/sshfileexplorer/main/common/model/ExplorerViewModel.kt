package com.emindev.sshfileexplorer.main.common.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.sshfileexplorer.helperlibrary.common.helper.StringHelper
import com.emindev.sshfileexplorer.helperlibrary.common.model.Resource
import com.emindev.sshfileexplorer.main.common.util.ExplorerUtil
import kotlinx.coroutines.flow.*

class ExplorerViewModel() : ViewModel() {


    private val _currentListArray = ArrayList<String>()
    private val _currentPathList = MutableStateFlow(ArrayList<String>())
    val currentPathList = _currentPathList.asStateFlow()

    private val _currentPathString = MutableStateFlow(StringHelper.delimiter)
    val currentPathString = _currentPathString.asStateFlow()

    private val _resource = MutableStateFlow<Resource<List<FileModel>>>(Resource.Success(emptyList()))
    val resource = _resource.asStateFlow()


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
            var newPath = StringHelper.delimiter
            _currentListArray.forEach { path ->
                newPath += path + StringHelper.delimiter
            }
            _currentPathString.value = newPath
            ExplorerUtil.foldersInPath(newPath) { folderResource ->
                val sourceList = ArrayList<FileModel>()

                when(folderResource){
                    is Resource.Error -> _resource.value = folderResource
                    is Resource.Loading -> _resource.value = folderResource
                    is Resource.Success -> {
                        sourceList.addAll(folderResource.data?: emptyList())
                        ExplorerUtil.filesInPath(newPath){fileResource ->
                            when(fileResource){
                                is Resource.Error -> _resource.value = fileResource
                                is Resource.Loading -> _resource.value = fileResource
                                is Resource.Success ->{
                                    sourceList.addAll(fileResource.data?: emptyList())
                                    _resource.value = Resource.Success(sourceList)
                                }
                            }
                        }
                    }
                }

            }


        }.launchIn(viewModelScope)

    }


}
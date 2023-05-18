package com.emindev.sshfileexplorer.main.common.model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialogViewModel : ViewModel() {
    private val _loadingDialog = MutableStateFlow(false)
    val loadingDialog = _loadingDialog.asStateFlow()

    private val _errorDialog = MutableStateFlow(ErrorDialogModel(false,"",""))
    val errorDialog = _errorDialog.asStateFlow()

    fun hideAllDialogs() {
        _loadingDialog.value = false
        _errorDialog.value = ErrorDialogModel(false,"","")
    }


    fun showErrorDialog(errorModel:ErrorDialogModel) {
        hideAllDialogs()
        _errorDialog.value = errorModel
    }

    fun showLoadingDialog() {
        hideAllDialogs()
        _loadingDialog.value = true
    }

}
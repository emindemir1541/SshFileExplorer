package com.emindev.sshfileexplorer.main.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.emindev.sshfileexplorer.R
import com.emindev.sshfileexplorer.main.common.model.DialogViewModel
import com.emindev.sshfileexplorer.main.common.model.ErrorDialogModel

@Composable
fun LoadingDialog(loadingDialogModel: State<Boolean>) {
    if (loadingDialogModel.value)
        Dialog(onDismissRequest = { }, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
}

@Composable
fun ErrorDialog(errorDialogModel: State<ErrorDialogModel>, dialogViewModel: DialogViewModel) {
    if (errorDialogModel.value.isShowing)
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = errorDialogModel.value.title) },
            text = { Text(text = errorDialogModel.value.message) },
            confirmButton = {
                TextButton(onClick = { dialogViewModel.hideAllDialogs() }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )
}

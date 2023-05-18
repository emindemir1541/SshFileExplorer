package com.emindev.sshfileexplorer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.emindev.sshfileexplorer.main.common.model.ExplorerViewModel
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceDatabase
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceViewModel
import com.emindev.sshfileexplorer.main.ui.page.ExplorerPage
import com.emindev.sshfileexplorer.main.ui.page.MainPage
import com.emindev.sshfileexplorer.main.ui.page.Navigation
import com.emindev.sshfileexplorer.main.ui.theme.SSHFileExplorerTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(applicationContext, DeviceDatabase::class.java, "devices.db").build()
    }
    private val deviceViewModel by viewModels<DeviceViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DeviceViewModel(db.dao) as T
                }
            }
        }
    )

    private val explorerViewModel by viewModels<ExplorerViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SSHFileExplorerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val state by deviceViewModel.state.collectAsState()

                    Navigation(state = state, onEvent = deviceViewModel::onEvent, explorerViewModel = explorerViewModel)
                }

            }
        }
    }
}


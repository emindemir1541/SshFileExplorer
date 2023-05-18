package com.emindev.sshfileexplorer.main.ui.page

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.emindev.sshfileexplorer.main.common.constant.Page
import com.emindev.sshfileexplorer.main.common.model.DialogViewModel
import com.emindev.sshfileexplorer.main.common.model.ExplorerViewModel
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceEvent
import com.emindev.sshfileexplorer.main.data.sshrepository.DeviceState
import com.emindev.sshfileexplorer.main.ui.animation.animatedComposable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(state: DeviceState, onEvent: (DeviceEvent) -> Unit,explorerViewModel: ExplorerViewModel,dialogViewModel: DialogViewModel) {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Page.Main.route) {

        composable(route = Page.Main.route){
        MainPage(navController,state,onEvent,dialogViewModel)
        }

        animatedComposable(route = Page.Explorer.route){
            ExplorerPage(navController,explorerViewModel,onEvent)
        }
    }

}
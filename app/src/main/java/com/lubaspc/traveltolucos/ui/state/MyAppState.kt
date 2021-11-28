package com.lubaspc.traveltolucos.ui.state

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lubaspc.traveltolucos.MTViewModel

class MyAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val vModel: MTViewModel,
    private val resources: Resources,

) {

}

@Composable
fun rememberMyAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    vModel: MTViewModel = viewModel(),
    resources: Resources = LocalContext.current.resources,
) = remember(scaffoldState, navController, resources) {
    MyAppState(scaffoldState, navController,vModel, resources)
}
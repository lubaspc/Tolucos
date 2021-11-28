package com.lubaspc.traveltolucos.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.lubaspc.traveltolucos.MainActivity



@Composable
fun MainActivity.SettingsView() {
    val showDialog = remember { mutableStateOf(true) }

}
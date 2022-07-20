package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

data class Amount(
    var date: MutableState<Date?> = mutableStateOf(null),
    var amount: MutableState<String> = mutableStateOf("")
)
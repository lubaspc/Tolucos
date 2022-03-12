package com.lubaspc.traveltolucos.model

import android.text.BoringLayout
import androidx.compose.runtime.MutableState

data class PersonModel(
    val personMD: PersonMD,
    val total: Double,
    val days: List<ChargeDayMD>,
    var show: MutableState<Boolean>
)
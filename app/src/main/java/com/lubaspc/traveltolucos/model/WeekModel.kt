package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

data class WeekModel(
    val monday: Calendar,
    val sunday: Calendar,
    val totalWeek: Double,
    var show: MutableState<Boolean>,
    var completePay: MutableState<Boolean>,
    val persons: List<PersonModel>,
    val daysXPerson: List<ChargeDayMD>
)
package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

data class WeekModel(
    val monday: Calendar,
    val sunday: Calendar,
    val totalWeek: Double,
    var show: Boolean = false,
    var completePay: Boolean = true,
    val persons: List<PersonModel>,
)

data class PersonModel(
    val person: String,
    val phone: String,
    val total: Double,
    var show: Boolean = false,
    var completePay: Boolean = true,
    val days: List<DayModel>
)

data class DayModel(
    val day : Calendar,
    val total: Double,
    val noPersons: Int,
    val charges: List<ChargeModel>
)

data class ChargeModel(
    val idChargePerson: Long,
    val description: String,
    val total: Double,
    val payment: Double
)
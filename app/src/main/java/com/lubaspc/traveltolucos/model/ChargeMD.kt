package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.lubaspc.traveltolucos.room.TypeCharge

data class ChargeMD(
    var id: Long,
    var description: String,
    var price: Double,
    var amount: Int,
    var type: TypeCharge,
    var total: MutableState<Double> = mutableStateOf(0.0) ,
    var checked: MutableState<Boolean> = mutableStateOf(false)
)

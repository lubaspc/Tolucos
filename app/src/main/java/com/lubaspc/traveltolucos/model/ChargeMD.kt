package com.lubaspc.traveltolucos.model

import com.lubaspc.traveltolucos.room.TypeCharge

data class ChargeMD(
    var id: Long,
    var description: String,
    var price: Double,
    var amount: Int,
    var type: TypeCharge,
    var checked: Boolean = false,
){
    val total: Double
        get() = price * amount


}

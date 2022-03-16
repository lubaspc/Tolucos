package com.lubaspc.traveltolucos.model

import java.util.*

data class ChargeDayMD(
    val idChargePerson: Long,
    var personIdFk: Long,
    var day: Calendar,
    var total: Double,
    var payment: Double,
    var pay: Boolean,
    var description: String,
    var person: PersonMD
)
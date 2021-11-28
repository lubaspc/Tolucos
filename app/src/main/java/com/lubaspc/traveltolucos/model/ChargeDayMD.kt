package com.lubaspc.traveltolucos.model

import java.util.*

data class ChargeDayMD(
    var chargeIdFk: Long,
    var personIdFk: Long,
    var day: Calendar,
    var total: Double,
    var payment: Double,
    var pay: Boolean,
    var charge: ChargeMD,
    var person: PersonMD
)
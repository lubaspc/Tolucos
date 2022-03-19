package com.lubaspc.traveltolucos.utils

import com.lubaspc.traveltolucos.model.PersonMD
import com.lubaspc.traveltolucos.room.TypeCharge

val Number.formatPrice
    get() = "$" + String.format("%.2f", this)

fun PersonMD.getTotal(size: Int) = listCharges.filter { it.type == TypeCharge.GROUP && checked }
    .sumOf { it.total / size } + listCharges.filter { it.type == TypeCharge.PERSONAL && checked }
    .sumOf { it.total }
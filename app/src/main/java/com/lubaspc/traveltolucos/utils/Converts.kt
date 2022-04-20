package com.lubaspc.traveltolucos.utils

import com.lubaspc.traveltolucos.model.ChargeMD
import com.lubaspc.traveltolucos.model.PersonMD
import com.lubaspc.traveltolucos.room.ChargeDb
import com.lubaspc.traveltolucos.room.PersonDb

val PersonDb.toMd
    get() = PersonMD(personId, name, phone)

val ChargeDb.toMd
    get() =  ChargeMD(chargeId, description, price, amount,type)

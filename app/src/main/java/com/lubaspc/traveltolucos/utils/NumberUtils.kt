package com.lubaspc.traveltolucos.utils

val Number.formatPrice
    get() = "$"+String.format("%.2f",this)
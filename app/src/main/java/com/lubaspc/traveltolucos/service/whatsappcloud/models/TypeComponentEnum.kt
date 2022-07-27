package com.lubaspc.traveltolucos.service.whatsappcloud.models

import com.google.gson.annotations.SerializedName

enum class TypeComponentEnum {
    @SerializedName("header")
    HEADER,
    @SerializedName("body")
    BODY,
    @SerializedName("button")
    BUTTON

}
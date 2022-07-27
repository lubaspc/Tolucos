package com.lubaspc.traveltolucos.service.whatsappcloud.models

import com.google.gson.annotations.SerializedName

enum class TypeMessageEnum {
    @SerializedName("text")
    TEXT,
    @SerializedName("image")
    IMAGE,
    @SerializedName("template")
    TEMPLATE,
    @SerializedName("payload")
    PAYLOAD
}
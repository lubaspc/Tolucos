package com.lubaspc.traveltolucos.service.whatsappcloud.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ValidateReadMessage(
    @SerializedName("message_id")
    @Expose
    var messageId: String,
    @SerializedName("messaging_product")
    @Expose
    var messagingProduct: String = "whatsapp",
    @SerializedName("status")
    @Expose
    var status: String = "read"
)
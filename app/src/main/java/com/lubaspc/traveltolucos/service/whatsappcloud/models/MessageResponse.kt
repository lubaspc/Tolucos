package com.lubaspc.traveltolucos.service.whatsappcloud.models

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("messaging_product")
    var messaginProduct: String,
    @SerializedName("contacts")
    var contacts: List<Contacts>,
    var messages: List<Message>
){
    data class Message(
        var id: String
    )
    data class Contacts(
        @SerializedName("input")
        var input: String,
        @SerializedName("wa_id")
        var waId: String
    )
}
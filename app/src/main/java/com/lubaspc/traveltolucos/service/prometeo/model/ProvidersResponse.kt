package com.lubaspc.traveltolucos.service.prometeo.model

import com.google.gson.annotations.SerializedName

data class ProvidersResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("providers")
    val providers: List<Provider>
){
    data class Provider(
        val code: String,
        val name: String,
        val country: String
    )
}

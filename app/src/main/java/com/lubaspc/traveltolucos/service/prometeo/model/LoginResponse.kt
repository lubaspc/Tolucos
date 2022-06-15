package com.lubaspc.traveltolucos.service.prometeo.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponse(
    @SerializedName("context")
    @Expose
    var context: String?,
    @SerializedName("field")
    @Expose
    var newCampo: String?,
    @SerializedName("key")
    @Expose
    var key: String?,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("message")
    @Expose
    val message: String?,
)
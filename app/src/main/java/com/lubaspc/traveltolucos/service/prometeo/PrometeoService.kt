package com.lubaspc.traveltolucos.service.prometeo

import com.lubaspc.traveltolucos.service.prometeo.model.LoginResponse
import com.lubaspc.traveltolucos.service.prometeo.model.ProvidersResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PrometeoService {

    @GET("provider/")
    suspend fun getProviders(): ProvidersResponse

    @POST("login/")
    @FormUrlEncoded
    suspend fun login(
        @Field("provider") provider: String,
        @Field("username") userName: String,
        @Field("password") pass: String,
        @Field("otp") otp: String? = null
    ): LoginResponse
}
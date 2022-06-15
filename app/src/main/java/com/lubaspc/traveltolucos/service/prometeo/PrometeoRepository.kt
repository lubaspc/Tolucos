package com.lubaspc.traveltolucos.service.prometeo

import com.google.gson.GsonBuilder
import com.lubaspc.traveltolucos.service.GasRepository
import com.lubaspc.traveltolucos.service.GenericResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PrometeoRepository {
    private val apiKey = "CJFFFczxyEVODvy0gTnFzfU8kgktDi0gHSRM1Y4mYOBIe7djLtKS2fE2NqFSHJ0K"
    private val codeBbvaMx = "bbva_mx"

    private val api by lazy {
        Retrofit.Builder()
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(120L, TimeUnit.SECONDS)
                    .callTimeout(120L, TimeUnit.SECONDS)
                    .readTimeout(120L, TimeUnit.SECONDS)
                    .writeTimeout(120L, TimeUnit.SECONDS)
                    .addInterceptor {
                        val request = it.request().newBuilder()
                            .addHeader("X-API-Key", apiKey)
                            .build()
                        it.proceed(request)
                    }
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .baseUrl("https://banking.sandbox.prometeoapi.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .serializeNulls()
                        .create()
                )
            )
            .build()
            .create(PrometeoService::class.java)
    }

    private suspend fun <T> onResponse(cb: suspend () -> T): GenericResponse<T> =
        try {
            cb().run { GenericResponse(200, this, "") }
        } catch (e: Exception) {
            when (e) {
                is HttpException ->
                    GenericResponse(e.code(), null, e.message())
                else ->
                    GenericResponse(500, null, e.message)
            }
        }

    suspend fun getProviders() = onResponse { api.getProviders() }

    suspend fun login() = onResponse { api.login(codeBbvaMx, "12345pq", "asdfg") }
}
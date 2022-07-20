package com.lubaspc.traveltolucos.service

import android.annotation.SuppressLint
import com.google.gson.*
import com.lubaspc.traveltolucos.service.model.GasItemResponse
import com.lubaspc.traveltolucos.service.telegramBot.BotService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GasRepository {
    interface GASService {
        @FormUrlEncoded
        @POST("api/consulta_precios.php")
        suspend fun getPrices(
            @Field("url") url: String = "https://petrointelligence.com/api/api_precios.html?consulta=nac",
            @Query("consulta") consult: String = "nac"
        ): List<GasItemResponse>
    }

    private val api by lazy {
        Retrofit.Builder()
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(120L, TimeUnit.SECONDS)
                    .callTimeout(120L, TimeUnit.SECONDS)
                    .readTimeout(120L, TimeUnit.SECONDS)
                    .writeTimeout(120L, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .baseUrl("https://petrointelligence.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .serializeNulls()
                        .create()
                )
            )
            .build()
            .create(GASService::class.java)
    }


    suspend fun getPricePremium() = api.getPrices()
        .firstOrNull { it.tipoCombustible == "Premium" }?.precioPromedio?.toDoubleOrNull()?.plus(1)
}
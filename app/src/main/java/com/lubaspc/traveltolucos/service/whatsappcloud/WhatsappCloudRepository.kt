package com.lubaspc.traveltolucos.service.whatsappcloud

import com.google.gson.GsonBuilder
import com.lubaspc.traveltolucos.service.GenericResponse
import com.lubaspc.traveltolucos.service.whatsappcloud.models.MessageRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class WhatsappCloudRepository {
    private val version = "v13.0"
    private val idWhatsapp = "105695522235716" //729 367 0696
    private val token =
        "EAALMsaMs86ABAFqKqZCRUeNZCHbwHhXvZAE0LzfoyqpGpJo15aUOogHMNmvWNdLAQZA8ZAMUwemh0rmAbBCPDHur1druZCy507VGCtUP9CHFcMfrf4zYhXTvX6MU3rvKEog0n1v6TfOj7Bu0gzEUFvSaYZBhTeibt2npIMZCR1dIGvnu4tISL6u7dzUQpCmluoZAY7sn8uZBDUPwZDZD"

    private val api by lazy {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(120L, TimeUnit.SECONDS)
            .callTimeout(120L, TimeUnit.SECONDS)
            .readTimeout(120L, TimeUnit.SECONDS)
            .writeTimeout(120L, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()
                return@addInterceptor it.proceed(
                    request.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                )
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl("https://graph.facebook.com/$version/$idWhatsapp/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
//                        .registerTypeAdapter(
//                            Calendar::class.java,
//                            object : JsonDeserializer<Calendar> {
//                                @SuppressLint("SimpleDateFormat")
//                                override fun deserialize(
//                                    json: JsonElement?,
//                                    typeOfT: Type?,
//                                    context: JsonDeserializationContext?
//                                ) = Calendar.getInstance().apply {
//                                    timeInMillis = json?.asJsonPrimitive?.asLong ?: 0L
//                                }
//                            })
//                        .registerTypeAdapter(
//                            Calendar::class.java,
//                            object : JsonSerializer<Calendar> {
//                                override fun serialize(
//                                    src: Calendar?,
//                                    typeOfSrc: Type?,
//                                    context: JsonSerializationContext?
//                                ) = JsonPrimitive(src?.timeInMillis ?: 0L)
//                            })
                        .create()
                )
            )
            .build()
            .create(WhatsappCloudService::class.java)
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

    suspend fun sendMessage(body: MessageRequest) = onResponse { api.sendMessage(body) }

    suspend fun uploadImage(photo: File) = onResponse {
        api.uploadImage(
            MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("messaging_product", "whatsapp")
                .addFormDataPart(
                    "file",
                    photo.name,
                    photo.asRequestBody("image/jpeg".toMediaType())
                )
                .build()
        )
    }

}
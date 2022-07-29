package com.lubaspc.traveltolucos.service.telegramBot

import android.annotation.SuppressLint
import androidx.core.content.edit
import com.google.gson.*
import com.lubaspc.traveltolucos.App
import com.lubaspc.traveltolucos.service.GenericResponse
import com.lubaspc.traveltolucos.service.PaseTagService
import com.lubaspc.traveltolucos.service.telegramBot.models.EntityMessageRequest
import com.lubaspc.traveltolucos.utils.botToken
import com.lubaspc.traveltolucos.utils.groupId
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BotRepository {


    private val api by lazy {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(120L, TimeUnit.SECONDS)
            .callTimeout(120L, TimeUnit.SECONDS)
            .readTimeout(120L, TimeUnit.SECONDS)
            .writeTimeout(120L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl("https://api.telegram.org/bot$botToken/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(Calendar::class.java,
                            object : JsonDeserializer<Calendar> {
                                @SuppressLint("SimpleDateFormat")
                                override fun deserialize(
                                    json: JsonElement?,
                                    typeOfT: Type?,
                                    context: JsonDeserializationContext?
                                ) = Calendar.getInstance().apply {
                                    timeInMillis = json?.asJsonPrimitive?.asLong ?: 0L
                                }
                            })
                        .registerTypeAdapter(Calendar::class.java,
                            object : JsonSerializer<Calendar> {
                                override fun serialize(
                                    src: Calendar?,
                                    typeOfSrc: Type?,
                                    context: JsonSerializationContext?
                                ) = JsonPrimitive(src?.timeInMillis ?: 0L)
                            })
                        .create()
                )
            )
            .build()
            .create(BotService::class.java)
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

    suspend fun getMe() = onResponse { api.getMe() }

    suspend fun sendMessage(message: String, idGroup: String = groupId) =
        onResponse { api.sendMessage(idGroup, message) }

    suspend fun editMessage(
        photo: File,
        message: String,
        messageId: String,
        idGroup: String = groupId
    ) =
        onResponse {
            api.editMessagePhoto(
                MultipartBody.Part.createFormData(
                    "photo", photo.name, photo.asRequestBody("image/png".toMediaType())
                ),
                JsonObject().apply {
                    addProperty("type", "photo")
                    addProperty("media", "attach://photo")
                    addProperty("caption", message)
                    addProperty("parse_mode", "HTML")
                },
                idGroup,
                messageId,
            )
        }

    suspend fun deleteMessage(messageId: String, chatId: String = groupId) =
        onResponse { api.deleteMessage(messageId, chatId) }


    suspend fun sendPhoto(photo: File, caption: String, parseMode: String = "HTML") =
        onResponse {
            api.sendPhoto(
                MultipartBody.Part.createFormData(
                    "photo", photo.name, photo.asRequestBody("image/png".toMediaType())
                ),
                groupId,
                caption,
                parseMode
            )
        }

}
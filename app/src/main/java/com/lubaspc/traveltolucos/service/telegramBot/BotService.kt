package com.lubaspc.traveltolucos.service.telegramBot

import com.google.gson.JsonObject
import com.lubaspc.traveltolucos.service.telegramBot.models.EntityMessageRequest
import com.lubaspc.traveltolucos.service.telegramBot.models.TBotSendMessaeResponse
import com.lubaspc.traveltolucos.service.telegramBot.models.TBotSendPhotoResponse
import com.lubaspc.traveltolucos.service.telegramBot.models.TBotUpdatesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*

interface BotService {

    @Multipart
    @POST("sendPhoto")
    suspend fun sendPhoto(
        @Part filePart: MultipartBody.Part,
        @Query("chat_id") chatId: String,
        @Query("caption") caption: String,
        @Query("parse_mode") parseMode: String = "HTML",
    ): TBotSendPhotoResponse

    @GET("getMe")
    suspend fun getMe(): TBotUpdatesResponse

    @Multipart
    @POST("editMessageMedia")
    suspend fun editMessagePhoto(
        @Part filePart: MultipartBody.Part,
        @Part("media") media: JsonObject,
        @Query("chat_id") chatId: String,
        @Query("message_id") messageId: String,

    ): TBotSendMessaeResponse

    @GET("sendMessage")
    suspend fun sendMessage(
        @Query("chat_id") chatId: String,
        @Query("text") message: String,
        @Query("parse_mode") mode: String = "HTML"
    ): TBotSendMessaeResponse


    @GET("deleteMessage")
    suspend fun deleteMessage(
        @Query("chat_id") chatId: String,
        @Query("message_id") message: String,
    ): TBotSendMessaeResponse


    @GET("SuccessfulPayment")
    suspend fun successfulPayment(
        @Query("total_amount") total: Long,
        @Query("currency") currency: String = "MXN",
    )


}


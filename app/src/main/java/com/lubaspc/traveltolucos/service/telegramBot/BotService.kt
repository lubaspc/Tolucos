package com.lubaspc.traveltolucos.service.telegramBot

import com.lubaspc.traveltolucos.service.telegramBot.models.EntityMessageRequest
import com.lubaspc.traveltolucos.service.telegramBot.models.TBotSendMessaeResponse
import com.lubaspc.traveltolucos.service.telegramBot.models.TBotUpdatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BotService {

    @GET("getMe")
    suspend fun getMe(): TBotUpdatesResponse

    @GET("sendMessage")
    suspend fun sendMessage(
        @Query("chat_id") chatId: String,
        @Query("text") message: String,
        @Query("parse_mode") mode: String = "HTML"
    ): TBotSendMessaeResponse
}
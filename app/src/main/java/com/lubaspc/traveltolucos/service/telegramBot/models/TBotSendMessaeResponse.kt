package com.lubaspc.traveltolucos.service.telegramBot.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class TBotSendMessaeResponse(
    @SerializedName("ok")
    @Expose
    var ok: Boolean,
    @SerializedName("result")
    @Expose
    var result: TBotResultResponse
) {
    data class TBotResultResponse(
        @SerializedName("chat")
        @Expose
        var chat: TBotChatResponse,
        @SerializedName("date")
        @Expose
        var date: Int,
        @SerializedName("from")
        @Expose
        var from: TBotFromResponse,
        @SerializedName("message_id")
        @Expose
        var messageId: Int,
        @SerializedName("text")
        @Expose
        var text: String
    ) {
        data class TBotChatResponse(
            @SerializedName("id")
            @Expose
            var id: Int,
            @SerializedName("title")
            @Expose
            var title: String,
            @SerializedName("type")
            @Expose
            var type: String
        )

        data class TBotFromResponse(
            @SerializedName("first_name")
            @Expose
            var firstName: String,
            @SerializedName("id")
            @Expose
            var id: Long,
            @SerializedName("is_bot")
            @Expose
            var isBot: Boolean,
            @SerializedName("username")
            @Expose
            var username: String
        )
    }
}
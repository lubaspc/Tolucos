package com.lubaspc.traveltolucos.service.telegramBot.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class TBotSendPhotoResponse(
    @SerializedName("ok")
    @Expose
    var ok: Boolean,
    @SerializedName("result")
    @Expose
    var result: TBotResultResponse
) {
    data class TBotResultResponse(
        @SerializedName("caption")
        @Expose
        var caption: String,
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
        var messageId: String,
        @SerializedName("photo")
        @Expose
        var photo: List<TBotPhotoResponse>
    ) {
        data class TBotChatResponse(
            @SerializedName("id")
            @Expose
            var id: String,
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

        data class TBotPhotoResponse(
            @SerializedName("file_id")
            @Expose
            var fileId: String,
            @SerializedName("file_size")
            @Expose
            var fileSize: Int,
            @SerializedName("file_unique_id")
            @Expose
            var fileUniqueId: String,
            @SerializedName("height")
            @Expose
            var height: Int,
            @SerializedName("width")
            @Expose
            var width: Int
        )
    }
}
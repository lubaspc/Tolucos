package com.lubaspc.traveltolucos.service.whatsappcloud.models

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @SerializedName("messaging_product")
    var messaging_product: String = "whatsapp",
    @SerializedName("recipient_type")
    var recipientType: String = "individual",
    var to: String,
    var type: TypeMessageEnum = TypeMessageEnum.TEXT,
    var text: TextMessage? = null,
    var image: ImageMessage? = null,
    var context: Context? = null,
    var template: Template? = null
) {
    data class Context(
        @SerializedName("message_id")
        var messageId: String
    )

    data class ImageMessage(
        var id: String? = null,
        var link: String? = null
    )

    data class TextMessage(
        var body: String,
        @SerializedName("preview_url")
        var previewUrl: String
    )

    data class Template(
        val language: Language = Language(),
        val name: String,
        var components: List<Component>
    ) {
        data class Component(
            var type: TypeComponentEnum,
            var parameters: List<Parameter>,
            @SerializedName("sub_type")
            var subType: String? = null,
            var index: Int? = null,
        ) {
            data class Parameter(
                var type: TypeMessageEnum,
                var text: String? = null,
                var image: ImageMessage? = null,
                var payload: String? = null
            )
        }
    }

    data class Language(
        val code: String = "es"
    )
}
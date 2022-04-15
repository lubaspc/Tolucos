package com.lubaspc.traveltolucos.service.telegramBot.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class TBotUpdatesResponse(
    @SerializedName("ok")
    @Expose
    var ok: Boolean,
    @SerializedName("result")
    @Expose
    var result: List<TBotResultResponse>
) {
    data class TBotResultResponse(
        @SerializedName("message")
        @Expose
        var message: TBotMessageResponse,
        @SerializedName("my_chat_member")
        @Expose
        var myChatMember: TBotMyChatMemberResponse,
        @SerializedName("update_id")
        @Expose
        var updateId: Int
    ) {
        data class TBotMessageResponse(
            @SerializedName("chat")
            @Expose
            var chat: TBotChatResponse,
            @SerializedName("date")
            @Expose
            var date: Int,
            @SerializedName("entities")
            @Expose
            var entities: List<TBotEntityResponse>,
            @SerializedName("from")
            @Expose
            var from: TBotFromResponse,
            @SerializedName("message_id")
            @Expose
            var messageId: Int,
            @SerializedName("migrate_from_chat_id")
            @Expose
            var migrateFromChatId: Int,
            @SerializedName("migrate_to_chat_id")
            @Expose
            var migrateToChatId: Int,
            @SerializedName("new_chat_title")
            @Expose
            var newChatTitle: String,
            @SerializedName("sender_chat")
            @Expose
            var senderChat: TBotSenderChatResponse,
            @SerializedName("text")
            @Expose
            var text: String
        ) {
            data class TBotChatResponse(
                @SerializedName("all_members_are_administrators")
                @Expose
                var allMembersAreAdministrators: Boolean,
                @SerializedName("first_name")
                @Expose
                var firstName: String,
                @SerializedName("id")
                @Expose
                var id: Int,
                @SerializedName("last_name")
                @Expose
                var lastName: String,
                @SerializedName("title")
                @Expose
                var title: String,
                @SerializedName("type")
                @Expose
                var type: String,
                @SerializedName("username")
                @Expose
                var username: String
            )

            data class TBotEntityResponse(
                @SerializedName("length")
                @Expose
                var length: Int,
                @SerializedName("offset")
                @Expose
                var offset: Int,
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
                var id: Int,
                @SerializedName("is_bot")
                @Expose
                var isBot: Boolean,
                @SerializedName("language_code")
                @Expose
                var languageCode: String,
                @SerializedName("last_name")
                @Expose
                var lastName: String,
                @SerializedName("username")
                @Expose
                var username: String
            )

            data class TBotSenderChatResponse(
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
        }

        data class TBotMyChatMemberResponse(
            @SerializedName("chat")
            @Expose
            var chat: TBotChatResponse,
            @SerializedName("date")
            @Expose
            var date: Int,
            @SerializedName("from")
            @Expose
            var from: TBotFromResponse,
            @SerializedName("new_chat_member")
            @Expose
            var newChatMember: TBotNewChatMemberResponse,
            @SerializedName("old_chat_member")
            @Expose
            var oldChatMember: TBotOldChatMemberResponse
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
                var id: Int,
                @SerializedName("is_bot")
                @Expose
                var isBot: Boolean,
                @SerializedName("language_code")
                @Expose
                var languageCode: String,
                @SerializedName("last_name")
                @Expose
                var lastName: String,
                @SerializedName("username")
                @Expose
                var username: String
            )

            data class TBotNewChatMemberResponse(
                @SerializedName("can_be_edited")
                @Expose
                var canBeEdited: Boolean,
                @SerializedName("can_change_info")
                @Expose
                var canChangeInfo: Boolean,
                @SerializedName("can_delete_messages")
                @Expose
                var canDeleteMessages: Boolean,
                @SerializedName("can_invite_users")
                @Expose
                var canInviteUsers: Boolean,
                @SerializedName("can_manage_chat")
                @Expose
                var canManageChat: Boolean,
                @SerializedName("can_manage_voice_chats")
                @Expose
                var canManageVoiceChats: Boolean,
                @SerializedName("can_pin_messages")
                @Expose
                var canPinMessages: Boolean,
                @SerializedName("can_promote_members")
                @Expose
                var canPromoteMembers: Boolean,
                @SerializedName("can_restrict_members")
                @Expose
                var canRestrictMembers: Boolean,
                @SerializedName("is_anonymous")
                @Expose
                var isAnonymous: Boolean,
                @SerializedName("status")
                @Expose
                var status: String,
                @SerializedName("user")
                @Expose
                var user: TBotUserResponse
            ) {
                data class TBotUserResponse(
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

            data class TBotOldChatMemberResponse(
                @SerializedName("status")
                @Expose
                var status: String,
                @SerializedName("user")
                @Expose
                var user: TBotUserResponse
            ) {
                data class TBotUserResponse(
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
    }
}
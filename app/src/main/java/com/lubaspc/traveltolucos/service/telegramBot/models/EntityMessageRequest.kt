package com.lubaspc.traveltolucos.service.telegramBot.models

data class EntityMessageRequest(
    val type: String,
    val url: String,
    val text: String,
    val setOffset: Int,
    val setLength: Int
)

package com.lubaspc.traveltolucos.service

data class GenericResponse<T>(
    var errorCode: Int,
    var data: T?,
    var message: String?
)
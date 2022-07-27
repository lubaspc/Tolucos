package com.lubaspc.traveltolucos.service.whatsappcloud

import com.lubaspc.traveltolucos.service.whatsappcloud.models.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface WhatsappCloudService {

    @POST("messages")
    suspend fun sendMessage(@Body message: MessageRequest): MessageResponse

    @POST("media")
    suspend fun uploadImage(
        @Part body: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ): MediaResponse

    @POST("media")
    suspend fun uploadImage(
        @Body body: MultipartBody,
    ): MediaResponse

    @DELETE("{mediaId}")
    suspend fun deleteMedia(@Path("mediaId") id: String): GenericResponse

    @PUT("messages")
    suspend fun validateStatusMessage(@Body body: ValidateReadMessage): GenericResponse
}
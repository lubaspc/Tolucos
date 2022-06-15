package com.lubaspc.traveltolucos.service

import com.lubaspc.traveltolucos.service.model.*
import com.lubaspc.traveltolucos.service.model.Tag
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PaseTagService {

    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/aclaraciones")
    suspend fun consultaAclaracionesList(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int,
        @Query("desde") str2: String?,
        @Query("hasta") str3: String?
    ): Response<List<Aclaracion>>

    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/cruces")
    suspend fun consultarCruces(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int,
        @Query("desde") str2: String?,
        @Query("hasta") str3: String?,
        @Query("pendientes") z: Boolean
    ): Response<List<Movimiento>>

    @GET("cuentas/{id}")
    suspend fun consultarDatosCuenta(@Path("id") j: Long): Response<Usuario>

    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/facturas")
    suspend fun consultarFacturas(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int,
        @Query("desde") str2: String?,
        @Query("hasta") str3: String?
    ): Response<List<Factura>>

    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/movimientos")
    suspend fun consultarMovimientos(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String,
        @Path("numero") i: Int,
        @Query("desde") str2: String? = null,
        @Query("hasta") str3: String? = null
    ): Response<List<Movimiento>>


    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}")
    suspend fun consultarTag(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int
    ): Response<Tag>

    @Streaming
    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/facturas/{facturaId}/pdf")
    suspend fun descargarFactura(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int,
        @Path("facturaId") j2: Long
    ): Response<ResponseBody>

    @POST("../j_spring_security_logout")
    suspend fun logout(): Response<Void>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("j_username") str: String?,
        @Field("j_password") str2: String?,
        @Field("_spring_security_remember_me") z: Boolean
    ): Response<Long>

    @POST("cuentas/{cuentaId}/tags/{prefijo}/{numero}/adeudos")
    suspend fun reintentarCobroAdeudos(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int
    ): Response<Void>

    @GET("cuentas/{cuentaId}/tags/{prefijo}/{numero}/adeudos")
    suspend fun consultarAdeudos(
        @Path("cuentaId") j: Long,
        @Path("prefijo") str: String?,
        @Path("numero") i: Int
    ): Response<AdeudosResponse>

}
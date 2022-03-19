package com.lubaspc.traveltolucos.service

import android.annotation.SuppressLint
import androidx.core.content.edit
import com.google.gson.*
import com.lubaspc.traveltolucos.App
import com.lubaspc.traveltolucos.service.model.Tag
import com.lubaspc.traveltolucos.utils.parseDate
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import okhttp3.CookieJar
import retrofit2.Response
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


class RetrofitService {
    private var clientId: Long = 35497193
    private var user = "7225530820"
    private var pass = "5822772lpc"
    private var headersCookie =
        App.sharedPreferences.getStringSet(
            App.COOKIES,
            hashSetOf()
        ) ?: hashSetOf()


    private val api by lazy {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(120L, TimeUnit.SECONDS)
            .callTimeout(120L, TimeUnit.SECONDS)
            .readTimeout(120L, TimeUnit.SECONDS)
            .writeTimeout(120L, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()
                return@addInterceptor it.proceed(
                    request.newBuilder().header("User-Agent", "TuTag-Android")
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("Accept", "application/json")
                        .header("X-Api-Version", "2.5.0")
                        .header("Cookie", headersCookie.joinToString(";"))

                        .build()
                )
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl("https://tutag.pase.com.mx/sp-web/api/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(Date::class.java,
                            object : JsonDeserializer<Date> {
                                @SuppressLint("SimpleDateFormat")
                                override fun deserialize(
                                    json: JsonElement?,
                                    typeOfT: Type?,
                                    context: JsonDeserializationContext?
                                ) = Calendar.getInstance().apply {
                                    val asJsonPrimitive = json?.asJsonPrimitive
                                    if (asJsonPrimitive?.isNumber == true) {
                                        timeInMillis = asJsonPrimitive.asLong
                                    } else try {
                                        time =
                                            SimpleDateFormat("yyyy-MM-dd").parse(asJsonPrimitive?.asString)
                                    } catch (e: ParseException) {
                                        throw JsonParseException(e)
                                    }
                                }.time
                            })
                        .registerTypeAdapter(Date::class.java,
                            object : JsonSerializer<Date> {
                                override fun serialize(
                                    src: Date?,
                                    typeOfSrc: Type?,
                                    context: JsonSerializationContext?
                                ) = JsonPrimitive(src?.time ?: 0L)
                            })
                        .create()
                )
            )
            .build()
            .create(PaseTagService::class.java)
    }

    private suspend fun <T> onResponse(cb: suspend () -> Response<T>): GenericResponse<T> =
        try {
            cb().run {
                if (code() == 401) {
                    api.login(user, pass, true).apply {
                        if (code() in 200..299) {
                            clientId = body() ?: 0L
                            val setCookie = headers().values("set-cookie").toSet()
                            headersCookie = setCookie
                            App.sharedPreferences.edit {
                                putStringSet(App.COOKIES,setCookie)
                                clear()
                            }
                        }
                    }
                    onResponse(cb)
                } else GenericResponse(code(), body(), message())
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException ->
                    GenericResponse(e.code(), null, e.message())
                else ->
                    GenericResponse(500, null, e.message)
            }
        }


    suspend fun getAccount() = onResponse { api.consultarDatosCuenta(clientId) }

    suspend fun getMovements(tag: Tag, day: Calendar? = null) =
        onResponse {
            api.consultarMovimientos(
                clientId,
                tag.prefijo,
                tag.numero,
                day?.parseDate("yyyy-MM-dd"),
                day?.parseDate("yyyy-MM-dd")
            )
        }

    suspend fun getTag(tag: Tag) =
        onResponse { api.consultarTag(clientId, tag.prefijo, tag.numero) }


}
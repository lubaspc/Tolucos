package com.lubaspc.traveltolucos.service

import android.annotation.SuppressLint
import androidx.core.content.edit
import com.google.gson.*
import com.lubaspc.traveltolucos.App
import com.lubaspc.traveltolucos.service.model.Tag
import com.lubaspc.traveltolucos.utils.parseDate
import com.lubaspc.traveltolucos.utils.passPase
import com.lubaspc.traveltolucos.utils.userPase
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class RetrofitService {
    private var headersCookie =
        App.sharedPreferences.getStringSet(
            App.COOKIES,
            hashSetOf()
        ) ?: hashSetOf()
    private var clientId =
        App.sharedPreferences.getString("CLIENT_ID", "0")?.toLong() ?: 0L


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
                    api.login(userPase, passPase, true).apply {
                        if (code() in 200..299) {
                            val setCookie = headers().values("set-cookie").toSet()
                            clientId = body() ?: 0L
                            headersCookie = setCookie
                            App.sharedPreferences.edit {
                                putStringSet(App.COOKIES, setCookie)
                                putString("CLIENT_ID", clientId.toString())
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

    suspend fun getMovements(tag: Tag, day: String,dayHasta: String) =
        onResponse {
            api.consultarMovimientos(
                clientId,
                tag.prefijo,
                tag.numero,
                day,
                dayHasta
            )
        }

    suspend fun getTag(tag: Tag) =
        onResponse { api.consultarTag(clientId, tag.prefijo, tag.numero) }


}
package com.lubaspc.traveltolucos.service.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GasItemResponse(
    @SerializedName("fecha_consulta")
    @Expose
    var fechaConsulta: String,
    @SerializedName("hora_actualizacion")
    @Expose
    var horaActualizacion: String,
    @SerializedName("nacional")
    @Expose
    var nacional: String,
    @SerializedName("precio_maximo")
    @Expose
    var precioMaximo: String,
    @SerializedName("precio_minimo")
    @Expose
    var precioMinimo: String,
    @SerializedName("precio_promedio")
    @Expose
    var precioPromedio: String,
    @SerializedName("tipo_combustible")
    @Expose
    var tipoCombustible: String,
    @SerializedName("total_gasolineras")
    @Expose
    var totalGasolineras: String
)

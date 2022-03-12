package com.lubaspc.traveltolucos.service.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.util.*

data class Movimiento(
    @SerializedName("carril")
    var carril: String,
    @SerializedName("caseta")
    @Expose
    var caseta: String,
    @SerializedName("facturado")
    @Expose
    var facturado: Boolean,
    @SerializedName("fecha")
    @Expose
    var fecha: Date,
    @SerializedName("fechaPago")
    @Expose
    var fechaPago: Date,
    @SerializedName("idCaseta")
    @Expose
    var idCaseta: Int,
    @SerializedName("monto")
    @Expose
    var monto: Double,
    @SerializedName("nomCarril")
    @Expose
    var nomCarril: String,
    @SerializedName("pagado")
    @Expose
    var pagado: Boolean,
    @SerializedName("tipo")
    @Expose
    var tipo: Int,
    @SerializedName("tramo")
    @Expose
    var tramo: String
) {
    enum class CrucesFilter {
        PENDIENTES, FACTURADOS
    }

    override fun toString(): String {
        val str: String = if (facturado) ",Facturado" else ",Por facturar"
        val locale = FormatUtils.locale
        val objArr = arrayOfNulls<Any>(5)
        //objArr[0] = fecha
        objArr[1] = if (tipo == 2) "Cruce" else "Recarga"
        objArr[2] = monto
        objArr[3] = caseta
        objArr[4] = str
        return String.format(
            locale,
            "%5\$d : [%1\$tF %1\$tR], %2\$s, monto=%3\$f, caseta=%4\$s %6\$s",
            *objArr
        )
    }
}
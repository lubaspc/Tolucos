package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.generateQR
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*

data class WeekModel(
    val monday: Calendar,
    val sunday: Calendar,
    val totalWeek: Double,
    var show: Boolean = false,
    var completePay: Boolean = true,
    val persons: List<PersonModel>,
)

data class PersonModel(
    val person: String,
    val phone: String,
    val total: Double,
    var show: Boolean = false,
    var completePay: Boolean = true,
    val days: List<DayModel>
) {

    fun messageWeek(pay: Boolean = false) =
        "<b>$person -  ${if (pay) "PAGADO" else "NO PAGADO"}</b>\n<b>Total Semana ${total.formatPrice}</b>\n\n${
            days.joinToString("\n") {
                "<b>${it.day.parseDate()}</b>: ${it.total.formatPrice}\n${
                    it.charges.joinToString(
                        "\n"
                    ) { "<b>-></b> ${it.description.take(12)}: ${it.total.formatPrice} = ${it.payment.formatPrice}" }
                }"
            }
        }"+""
}

data class DayModel(
    val day: Calendar,
    val total: Double,
    val noPersons: Int,
    val charges: List<ChargeModel>
)

data class ChargeModel(
    val idChargePerson: Long,
    val idMessage: String? = null,
    val description: String,
    val total: Double,
    val payment: Double
)
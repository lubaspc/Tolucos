package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewStructure
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.databinding.FragmentHistoryBinding
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.model.PersonModel
import com.lubaspc.traveltolucos.model.WeekModel
import com.lubaspc.traveltolucos.ui.Purple500
import com.lubaspc.traveltolucos.ui.Purple700
import com.lubaspc.traveltolucos.ui.Teal200
import com.lubaspc.traveltolucos.ui.WeekView
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.generateQR
import com.lubaspc.traveltolucos.utils.moveField
import com.lubaspc.traveltolucos.utils.parseDate
import kotlinx.coroutines.InternalCoroutinesApi
import java.net.URLEncoder
import java.util.*

class HistoryFragment : Fragment() {
    val vModel by activityViewModels<MTViewModel>()
    private val weekState = mutableStateListOf<WeekModel>()
    val refreshing = SwipeRefreshState(true)

    @InternalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                viewLifecycleOwner
            )
        )
        setContent {
            MaterialTheme(
                colors = lightColors(
                    primary = Purple500,
                    primaryVariant = Purple700,
                    secondary = Teal200
                ),
                content = { WeekView(weeks = weekState) }
            )
        }
    }

    fun refreshHistory() {
        refreshing.isRefreshing = true
        vModel.consultHistory()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.weeks.observe(this) {
            weekState.clear()
            weekState.addAll(it)
            refreshing.isRefreshing = false
        }
    }

    private val PersonModel.BotTmessage: String
        get() = "<b>Total Semana ${total.formatPrice}</b>\n\n${
            days.joinToString("\n") {
                "<b>${it.day.parseDate()}</b>: ${it.total.formatPrice}\n${
                    it.charges.joinToString(
                        "\n"
                    ) { "<b>-></b> ${it.description.take(12)}: ${it.total.formatPrice} = ${it.payment.formatPrice}" }
                }"
            }
        }".run {
            val weeksDeuda =
                vModel.weeks.value?.flatMap { w -> w.persons.filter { it.person == person && !it.completePay && it != this@BotTmessage } }
            var headerString = ""
            if (!weeksDeuda.isNullOrEmpty()) {
                headerString =
                    "<b>Montos de semanas anteriores</b>\n${weeksDeuda.joinToString("\n") { "<b>-> ${it.total.formatPrice}</b>" }}" +
                            "\n- - - - - - - - - - - - - - - - -\n"
            }
            val qr = generateQR(total + (weeksDeuda?.sumOf { it.total } ?: 0.0))
            return@run "$headerString$this\n- - - - - - - - - - - - - - - - -\n" +
                    "<a href=\"google.com\">Banco Azteca</a>\n\n"
//                    "<a href=\"baz://send_money/$qr/\">Baz</a>"
        }

    fun sendMessageWeekPerson(person: PersonModel) {
        vModel.sendMessage(person.BotTmessage)
    }

    fun sendMessageWeek(week: WeekModel) {
        week.persons.forEach { vModel.sendMessage(it.BotTmessage) }
    }

    fun showDialogChagePay(person: PersonModel) {
        if (context == null) return
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Confirmar pago")
            .setMessage(
                "Estas seguro de confirmar el pago de ${person.person} del monto ${person.total.formatPrice}"
            )
            .setPositiveButton("PAGAR") { d, _ ->
                d.dismiss()
                vModel.confirmPay(person)

            }.show()
    }
}
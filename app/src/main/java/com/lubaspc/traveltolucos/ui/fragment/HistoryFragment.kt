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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.lubaspc.traveltolucos.utils.parseDate
import java.net.URLEncoder

class HistoryFragment : Fragment() {
    private val vModel by activityViewModels<MTViewModel>()
    private val weekState = mutableStateListOf<WeekModel>()
    private lateinit var refresh: SwipeRefreshLayout

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =SwipeRefreshLayout(requireContext()).apply {
        refresh = this
        isRefreshing = true

        this.setOnRefreshListener(vModel::consultHistory)
        addView(ComposeView(requireContext()).apply {
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
        })
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.weeks.observe(this) {
            weekState.clear()
            weekState.addAll(it)
            refresh.isRefreshing = false
        }
    }

    private val PersonModel.message: String
        get() = "*Total Semana ${total.formatPrice}*\n\n${
            days.joinToString("\n") {
                "*${it.day.parseDate()}*: ${it.total.formatPrice}\n${
                    it.charges.joinToString(
                        "\n"
                    ) { "*->* ${it.description.take(12)}: ${it.total.formatPrice} = ${it.payment.formatPrice}" }
                }"
            }
        }"

    private val PersonModel.intent: Intent
        get() = Intent(Intent.ACTION_VIEW)
            .setPackage("com.whatsapp.w4b")
            .setData(
                Uri.parse(
                    "https://api.whatsapp.com/send?phone=+52${phone}&text=${
                        URLEncoder.encode(message, "UTF-8")
                    }"
                )
            )

    fun sendMessageWeekPerson(person: PersonModel) {
        if (context == null) return
        startActivity(person.intent)

    }

    fun sendMessageWeek(week: WeekModel) {
        if (context == null) return
        context?.startActivities(week.persons.map { it.intent }.toTypedArray())
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
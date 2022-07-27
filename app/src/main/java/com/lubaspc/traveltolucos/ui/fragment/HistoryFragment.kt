package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
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
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.FragmentHistoryBinding
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.model.PersonModel
import com.lubaspc.traveltolucos.model.WeekModel
import com.lubaspc.traveltolucos.ui.WeekView
import com.lubaspc.traveltolucos.utils.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.net.URLEncoder
import java.util.*

class HistoryFragment : Fragment() {
    val vModel by activityViewModels<MTViewModel>()
    private val weekState = mutableStateListOf<WeekModel>()
    val refreshing = SwipeRefreshState(true)

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
                colorScheme = if (isSystemInDarkTheme()) dynamicDarkColorScheme(inflater.context)
                else dynamicLightColorScheme(inflater.context),
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

    fun showDialogChagePay(person: PersonModel) {
        if (context == null) return
        val imgPay = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.pagado)
            .convertToFile()
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Confirmar pago")
            .setMessage(
                "Estas seguro de confirmar el pago de ${person.person} del monto ${person.total.formatPrice}"
            )
            .setPositiveButton("PAGAR") { d, _ ->
                d.dismiss()
                vModel.confirmPay(person, imgPay)
            }.show()
    }
}
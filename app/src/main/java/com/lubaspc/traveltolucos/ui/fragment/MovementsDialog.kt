package com.lubaspc.traveltolucos.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.service.model.Movimiento
import com.lubaspc.traveltolucos.utils.formatPrice

class MovementsDialog : DialogFragment() {
    private val vModel by activityViewModels<MTViewModel>()
    private val movements = mutableStateListOf<Movimiento>()
    private val datePicker by lazy {
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Rango de fechas")
            .setPositiveButtonText("Aceptar")
            .build()
            .apply {
                addOnPositiveButtonClickListener(vModel::getMovements)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(inflater.context).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                viewLifecycleOwner
            )
        )
        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors()
                else lightColors(),
                content = { contentMovements() }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vModel.movementsConsult.observe(this) {
            movements.clear()
            movements.addAll(it)
        }
    }

    @Composable
    private fun contentMovements() {
        Scaffold {
            Column() {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    datePicker.show(childFragmentManager, datePicker.tag)
                }) {
                    Text(text = "Seleccionar fecha")
                }
                Text(text = "Total casetas: ${movements.size} ", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Monto Total: ${movements.sumOf { it.monto }.formatPrice} ",
                    fontSize = 18.sp
                )
            }
        }

    }

    fun show(supportFragmentManager: FragmentManager) {
        super.show(supportFragmentManager, this::class.simpleName)
    }
}
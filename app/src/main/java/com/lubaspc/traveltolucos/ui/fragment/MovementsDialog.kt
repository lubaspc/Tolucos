package com.lubaspc.traveltolucos.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.model.Amount
import com.lubaspc.traveltolucos.service.model.Movimiento
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*

class MovementsDialog : Fragment() {

    private var itemSelect: Amount? = null
    private val vModel by activityViewModels<MTViewModel>()
    private val movements = mutableStateListOf<Movimiento>()
    private val amounts = mutableStateListOf<Amount>()
    private val textButton = mutableStateOf("Seleccionar fecha")

    private val datePicker by lazy {
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Rango de fechas")
            .setPositiveButtonText("Aceptar")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    textButton.value =
                        Date(it.first).parseDate("dd/MMM") + " - " +
                                Date(it.second).parseDate("dd/MMM")
                    vModel.getMovements(it)
                }
            }
    }
    private val datePickerOne by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona fecha")
            .setPositiveButtonText("Aceptar")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    itemSelect?.date?.value = Date(it)
                }
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
                colorScheme = if (isSystemInDarkTheme()) dynamicDarkColorScheme(inflater.context)
                else dynamicLightColorScheme(inflater.context),
                content = { ContentMovements() }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.movementsConsult.observe(this) {
            movements.clear()
            movements.addAll(it)
        }
    }

    @Preview
    @Composable
    private fun ContentMovements() {
        Surface {
            Column(Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(modifier = Modifier.fillMaxWidth(0.8f), onClick = {
                        datePicker.show(childFragmentManager, datePicker.tag)
                    }) {
                        Text(text = textButton.value, fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Text(
                        text = "Casetas: ${movements.size} ",
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Monto Total: ${movements.sumOf { it.monto }.formatPrice} ",
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn {
                    items(amounts) { item ->
                        Column(Modifier.fillMaxWidth()) {
                            Row {
                                IconButton(onClick = { amounts.remove(item) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = colorResource(android.R.color.system_accent1_100)
                                    )
                                }
                                TextButton(onClick = {
                                    itemSelect = item
                                    datePickerOne.show(
                                        childFragmentManager,
                                        datePickerOne.javaClass.simpleName
                                    )
                                }) {
                                    Text(
                                        text = item.date.value?.parseDate("yyyy-MMM-dd")
                                            ?: "Sin fecha",

                                        )
                                }
                                TextField(
                                    modifier = Modifier.weight(1f),
                                    value = item.amount.value,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = {
                                        item.amount.value = it
                                    })

                            }
                        }
                    }
                }

                Button(onClick = {
                    amounts.add(Amount())
                }) {
                    Text(text = "Agregar un campo")
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Monto total: ${(movements.sumOf { it.monto } + amounts.sumOf { it.amount.value.toDoubleOrNull() ?: 0.0 }).formatPrice}",
                    fontSize = 22.sp
                )

            }
        }

    }
}
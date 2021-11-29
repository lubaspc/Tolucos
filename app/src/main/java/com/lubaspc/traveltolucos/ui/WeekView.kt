package com.lubaspc.traveltolucos.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.lubaspc.traveltolucos.MainActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.ui.theme.Purple200
import com.lubaspc.traveltolucos.ui.theme.Purple700
import com.lubaspc.traveltolucos.ui.theme.Teal200
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.into
import com.lubaspc.traveltolucos.utils.moveField
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainActivity.WeekView() {
    val weeks = remember {
        vModel.history.map {
            it.day.moveField(Calendar.SUNDAY).run {
                Pair(moveField(Calendar.MONDAY, true), moveField(Calendar.FRIDAY, true))
            }
        }.distinctBy { it.first }
            .sortedByDescending { it.first }
    }

    Scaffold(
        topBar = {
            TopAppBar {
                textTitle("Historial")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 12.dp,
                    top = it
                        .calculateTopPadding()
                        .plus(14.dp)
                )
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            itemsIndexed(weeks) { i, week ->
                val showWeek = remember { mutableStateOf(i == 0) }

                val histories = remember {
                    vModel.history.filter { h -> h.day.into(week.first, week.second) }
                }
                val persons = remember {
                    mutableStateListOf(
                        *histories.map { p -> p.person }
                            .distinctBy { p -> p.name }
                            .toTypedArray()
                    )
                }
                val (totalWeek, _) =
                    remember { mutableStateOf(histories.sumOf { h -> h.payment }.formatPrice) }
                val excecuteWeeK = remember {
                    mutableStateListOf<Pair<String, String>>()
                }
                val completePay = remember {
                    !histories.any { j -> !j.pay }
                }
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = 10.dp,
                        backgroundColor = colorResource(id = R.color.teal_700),
                        contentColor = colorResource(id = R.color.white),
                        border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
                        onClick = {
                            if (showWeek.value && !completePay)
                                setMessage(excecuteWeeK.distinctBy { it.second })
                            else showWeek.value = !showWeek.value
                        }
                    ) {
                        Row {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (completePay) Teal200 else Purple200
                            )
                            textTitle(week.first.parseDate("dd") + "-" + week.second.parseDate())
                            Spacer(Modifier.weight(1f, true))
                            textTitle("Total $totalWeek")

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (showWeek.value) {
                        persons.map { p ->
                            val textSend = remember {
                                mutableStateListOf<String>()
                            }
                            val (totalPersonWeek, _) = remember {
                                mutableStateOf(
                                    histories
                                        .filter { h -> h.personIdFk == p.personId }
                                        .distinctBy { h -> h.day.parseDate() }
                                        .sumOf { h -> h.total }.formatPrice
                                )
                            }

                            textSend.add("*Total Semana: $totalPersonWeek*")

                            val days = remember {
                                mutableStateListOf(
                                    *histories.filter { h -> h.personIdFk == p.personId }
                                        .distinctBy { h -> h.day.parseDate() }
                                        .sortedBy { it.day }
                                        .toTypedArray()
                                )
                            }

                            val completePayPerson = remember {
                                !days.any { j -> !j.pay }
                            }

                            val showPerson = remember {
                                mutableStateOf(days.firstOrNull()?.pay != true)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                elevation = 10.dp,
                                backgroundColor = colorResource(id = R.color.purple_500),
                                border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
                                onClick = {
                                    if (showPerson.value && days.firstOrNull()?.pay == false)
                                        setMessage(textSend.distinct().joinToString("\n"), p.phone)
                                    else showPerson.value = !showPerson.value
                                }
                            ) {
                                Column {
                                    Row {
                                        IconButton(onClick = {
                                            if (days.firstOrNull()?.pay == false)
                                                showDialogChagePay(
                                                    days,
                                                    week.first.parseDate("dd") + "-" + week.second.parseDate()
                                                )
                                        }) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (days.firstOrNull()?.pay == true) Purple700 else Purple200
                                            )
                                        }
                                        textTitle(p.name)
                                        Spacer(Modifier.weight(1f, true))
                                        textTitle("T: $totalPersonWeek")
                                    }
                                    if (showPerson.value) {
                                        Divider(color = colorResource(id = R.color.teal_700))
                                        days.map { d ->
                                            val chargers = remember {
                                                mutableStateListOf(
                                                    *histories
                                                        .filter { h ->
                                                            h.personIdFk == p.personId && h.day.compareTo(
                                                                d.day
                                                            ) == 0
                                                        }
                                                        .toTypedArray()
                                                )
                                            }
                                            val personsSize = remember {
                                                histories.filter { h -> h.day.parseDate() == d.day.parseDate() }
                                                    .distinctBy { h -> h.personIdFk }.size + 1
                                            }
                                            textSend.add("   ${d.day.parseDate()}: ${d.total.formatPrice} P. $personsSize")
                                            Divider(color = colorResource(id = R.color.teal_700))
                                            Row {
                                                textTitle(d.day.parseDate(), Modifier.weight(1f))
                                                textTitle(d.total.formatPrice, Modifier.weight(1f))
                                                textTitle("P. $personsSize", Modifier.weight(1f))
                                            }
                                            Divider(color = colorResource(id = R.color.teal_700))
                                            chargers.map { c ->
                                                textSend.add("   -```${c.charge.description}: ${c.charge.total.value.formatPrice} = ${c.payment.formatPrice}```")
                                                Row {
                                                    textBody(
                                                        c.charge.description,
                                                        Modifier.weight(1f)
                                                    )
                                                    textBody(
                                                        c.charge.total.value.formatPrice,
                                                        Modifier.weight(1f)
                                                    )
                                                    textBody(
                                                        c.payment.formatPrice,
                                                        Modifier.weight(1f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            if (excecuteWeeK.firstOrNull { e -> e.second == p.phone } == null) {
                                excecuteWeeK.add(
                                    Pair(
                                        textSend.distinct().joinToString("\n"),
                                        p.phone
                                    )
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


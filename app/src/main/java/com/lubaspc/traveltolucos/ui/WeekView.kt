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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lubaspc.traveltolucos.MainActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.model.WeekModel
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.into
import com.lubaspc.traveltolucos.utils.moveField
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*


val Purple200 = Color(0xff62727b)
val Purple500 = Color(0xff62727b)
val Purple700 = Color(0xffb71c1c)
val Teal200 = Color(0xFF03DAC5)

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WeekView(weeks: List<WeekModel>) {
    Scaffold(
        backgroundColor = colorResource(id = R.color.black)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            itemsIndexed(weeks) { i, week ->
                val showWeek = remember { mutableStateOf(i == 0) }
                val completePay = remember { week.completePay }
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 10.dp,
                        backgroundColor = colorResource(id = R.color.teal_700),
                        contentColor = colorResource(id = R.color.white),
                        border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
                        onClick = {
                            if (showWeek.value && !completePay) {
                                //Send mesage
                            } else showWeek.value = !showWeek.value
                        }
                    ) {
                        Row {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (completePay) Teal200 else Purple200
                            )
                            textTitle(week.monday.parseDate("dd") + "-" + week.sunday.parseDate())
                            Spacer(Modifier.weight(1f, true))
                            textTitle("Total ${week.totalWeek.formatPrice}")

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (showWeek.value) {
                        week.persons.map { p ->
                            val showPerson = remember {
                                mutableStateOf(p.show)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                elevation = 10.dp,
                                backgroundColor = colorResource(id = R.color.teal_200),
                                border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
                                onClick = {
                                    if (showPerson.value) {
                                    }
                                    // setMessage(textSend.distinct().joinToString("\n"), p.phone)
                                    else showPerson.value = !showPerson.value
                                }
                            ) {
                                Column {
                                    Row {
                                        IconButton(onClick = {
                                            /* if (days.firstOrNull()?.pay == false)
                                                 showDialogChagePay(
                                                     days,
                                                     week.first.parseDate("dd") + "-" + week.second.parseDate()
                                                 )*/
                                        }) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Purple200
                                            )
                                        }
                                        textTitle(p.person)
                                        Spacer(Modifier.weight(1f, true))
                                        textTitle("T: ${p.total.formatPrice}")
                                    }
                                    if (showPerson.value) {
                                        Divider(color = colorResource(id = R.color.teal_700))
                                        p.days.map { d ->
                                            Divider(color = colorResource(id = R.color.teal_700))
                                            Row {
                                                textTitle(d.day.parseDate(), Modifier.weight(1f))
                                                textTitle(d.total.formatPrice, Modifier.weight(1f))
                                                textTitle("P. ${d.noPersons}", Modifier.weight(1f))
                                            }
                                            Divider(color = colorResource(id = R.color.teal_700))
                                            d.charges.map { c ->
                                                Row {
                                                    textBody(
                                                        c.description,
                                                        Modifier.weight(1f)
                                                    )
                                                    textBody(
                                                        c.total.formatPrice,
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
                        }
                    }
                }

            }
        }
    }

}


@Composable
fun textBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = modifier.padding(10.dp, 4.dp),
    )
}

@Composable
fun textTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = modifier.padding(10.dp, 4.dp),
        fontWeight = FontWeight.Bold,
    )
}



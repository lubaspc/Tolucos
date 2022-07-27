package com.lubaspc.traveltolucos.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.ColorRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.lubaspc.traveltolucos.MainActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.model.WeekModel
import com.lubaspc.traveltolucos.ui.fragment.HistoryFragment
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.into
import com.lubaspc.traveltolucos.utils.moveField
import com.lubaspc.traveltolucos.utils.parseDate
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryFragment.WeekView(weeks: List<WeekModel>) {
    Surface{
        SwipeRefresh(state = refreshing, onRefresh = this::refreshHistory) {
            LazyColumn(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                itemsIndexed(weeks) { i, week ->
                    val showWeek = remember { mutableStateOf(i == 0) }
                    Column {
                        Card(
                            onClick = {
                                if (showWeek.value && !week.completePay) {
                                    vModel.sendMessage(*week.persons.toTypedArray())
                                } else showWeek.value = !showWeek.value
                            },
//                            elevation = 10.dp,
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, colorResource(id = android.R.color.system_accent1_200)),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp, 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = colorResource(if (week.completePay) android.R.color.system_accent1_600 else  android.R.color.system_accent1_100)
                                )
                                textTitle(week.monday.parseDate("dd") + "-" + week.sunday.parseDate())
                                Spacer(Modifier.weight(1f, true))
                                textTitle("Total ${week.totalWeek.formatPrice}")

                            }
                        }
                        Spacer(modifier = Modifier.height(1.dp))
                        if (showWeek.value) {
                            week.persons.map { p ->
                                val showPerson = remember {
                                    mutableStateOf(p.show)
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
//                                    elevation = 10.dp,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(id = android.R.color.system_accent1_200)
                                    ),
                                    onClick = {
                                        if (showPerson.value && !p.completePay)
                                            vModel.sendMessage(p)
                                        else showPerson.value = !showPerson.value
                                    }
                                ) {
                                    Column {
                                        Row {
                                            IconButton(onClick = {
                                                if (!p.completePay)
                                                    this@WeekView.showDialogChagePay(p)
                                            }) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = colorResource(if (p.completePay) android.R.color.system_accent1_600 else  android.R.color.system_accent1_100)
                                                )
                                            }
                                            textTitle(p.person)
                                            Spacer(Modifier.weight(1f, true))
                                            textTitle("T: ${p.total.formatPrice}")
                                        }
                                        if (showPerson.value) {
                                            Divider(color = colorResource(id = android.R.color.system_accent1_200))
                                            p.days.map { d ->
                                                Divider(color = colorResource(id = android.R.color.system_accent1_200))
                                                Row {
                                                    textTitle(
                                                        d.day.parseDate(),
                                                        Modifier.weight(2f)
                                                    )
                                                    textTitle(
                                                        d.total.formatPrice,
                                                        Modifier.weight(1f)
                                                    )
                                                    textTitle(
                                                        "P. ${d.noPersons}",
                                                        Modifier.weight(1f)
                                                    )
                                                }
                                                Divider(color = colorResource(id = android.R.color.system_accent1_200))
                                                d.charges.map { c ->
                                                    Row {
                                                        textBody(
                                                            c.description,
                                                            Modifier.weight(2f)
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


}


@Composable
fun textBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = modifier.padding(10.dp, 4.dp),
        maxLines = 3
    )
}

@Composable
fun textTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 20.sp,
        modifier = modifier.padding(10.dp, 4.dp),
        fontWeight = FontWeight.Bold,
    )
}



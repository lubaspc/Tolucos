package com.lubaspc.traveltolucos.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import com.lubaspc.traveltolucos.MainActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.parseDate

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun MainActivity.SaveForm() {
    val (personsSize, _) = remember { mutableStateOf(vModel.persons.filter { p -> p.checked.value }.size + 1) }

    fun getTotal() =
        vModel.charges.sumOf {
            if (it.type == TypeCharge.GROUP && it.checked.value)
                it.total.value
            else 0.0
        }


    fun getSuperTotal() =
        vModel.persons.filter { p -> p.checked.value }.sumOf { p ->
            p.listCharges.sumOf { c ->
                if (c.checked.value) {
                    c.total.value / (if (c.type == TypeCharge.GROUP) personsSize
                    else 1)
                } else 0.0
            }
        }

    val total = remember { mutableStateOf(getTotal()) }
    val superTotal = remember { mutableStateOf(getSuperTotal()) }
    val (textToolbar, setTextToolbar) = remember { mutableStateOf("P: ${total.value}") }


    Scaffold(
        topBar = {
            TopAppBar {
                Text(
                    text = vModel.dateSelected.value.parseDate(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f, true))
                Button(onClick = {
                    setTextToolbar(
                        if (textToolbar.first() == 'P') "T: ${superTotal.value}"
                        else "P: ${total.value}"
                    )
                }) {
                    Text(text = textToolbar, fontSize = 24.sp)
                }
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = {
                    vModel.saveForm()
                    navController.navigate("home")
                }) {
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null)
                }
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
            items(vModel.persons.filter { p -> p.checked.value }) { p ->
                fun getSumPerson() =
                    p.listCharges.sumOf { c ->
                        if (c.checked.value) {
                            c.total.value / (if (c.type == TypeCharge.GROUP) personsSize
                            else 1)
                        } else 0.0
                    }
                val (totalPerson, setTotalPerson) = remember {
                    mutableStateOf(getSumPerson())
                }
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = 10.dp,
                        backgroundColor = colorResource(id = R.color.purple_500),
                        border = BorderStroke(1.dp, colorResource(id = R.color.teal_700))
                    ) {
                        Row {
                            textTitle(p.name)
                            Spacer(Modifier.weight(1f, true))
                            textTitle("Total ${totalPerson.formatPrice}")

                        }
                    }
                    p.listCharges.sortedBy { c -> c.type }.windowed(2, 2, true).map { chargesMin ->
                        Row {
                            chargesMin.map { charge ->
                                Card(
                                    border = BorderStroke(1.dp, colorResource(id = R.color.black)),
                                    modifier = Modifier
                                        .weight(1f, true)
                                        .padding(8.dp, 8.dp),
                                    backgroundColor = colorResource(id = if (charge.checked.value) R.color.red_dark else R.color.red_light),
                                    onClick = {
                                        if (charge.type == TypeCharge.GROUP) return@Card
                                        charge.checked.value = !charge.checked.value
                                        setTotalPerson(getSumPerson())
                                        superTotal.value = getSuperTotal()
                                    }
                                ) {
                                    Column {
                                        textTitle(charge.description)
                                        Divider(color = colorResource(id = R.color.black))
                                        Row {
                                            TextField(
                                                modifier = Modifier.weight(3f, true),
                                                value = "${charge.total.value}",
                                                enabled = charge.price <= 0.0 || charge.type == TypeCharge.PERSONAL,
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                onValueChange = { txt ->
                                                    charge.total.value = txt.toDoubleOrNull() ?: 0.0
                                                    setTotalPerson(getSumPerson())
                                                    superTotal.value = getSuperTotal()

                                                })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


package com.lubaspc.traveltolucos.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.lubaspc.traveltolucos.MainActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*
import androidx.compose.foundation.lazy.LazyVerticalGrid as LazyVerticalGrid1

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun MainActivity.HomeView() {
    val (dateSelect, _) = remember { vModel.dateSelected }
    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = {
                    navController.navigate("week")
                }) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
                Spacer(Modifier.weight(1f, true))
                Button(onClick = {
                    showDatePicker()
                }) {
                    Text(text = dateSelect.parseDate(), fontSize = 24.sp)
                }
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(Icons.Filled.Settings, contentDescription = null)
                }
            }
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 12.dp,
                    top = it
                        .calculateTopPadding()
                        .plus(14.dp)
                )
        ) {
            val (listCharge, buttonContinue) = createRefs()

            LazyColumn(modifier = Modifier.constrainAs(listCharge) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(buttonContinue.top)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }) {
                item {
                    Text(
                        text = stringResource(id = R.string.title_person),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(vModel.persons.windowed(3, 3, true)) { _, personsMinList ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        personsMinList.mapIndexed { idx, person ->
                            cardCheck(
                                person.checked.value,
                                person.name,
                                modifier = Modifier.weight(1f, true)
                            ) {
                                person.checked.value = !person.checked.value
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(id = R.string.title_cobors),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                items(vModel.movementsState.filter {m -> m.fecha.parseDate() == dateSelect.parseDate()  }){ m ->
                    Column {
                        Box(Modifier.size(50.dp).background(Color.Blue))
                        Text(text = "${m.tramo.take(5)}_${m.caseta.take(5)}")
                        Text(text = m.monto.formatPrice)
                    }
                }
                items(vModel.charges.filter { it.type == TypeCharge.GROUP }
                    .windowed(3, 3, true)) { chargesMinList ->
                    Row() {
                        chargesMinList.map { charge ->
                            cardCheck(
                                charge.checked.value,
                                charge.description,
                                modifier = Modifier.weight(1f, true)
                            ) {
                                charge.checked.value = !charge.checked.value
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    vModel.createSaveFrom()
                    navController.navigate("create_register")
                },
                Modifier
                    .constrainAs(buttonContinue) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
            ) {
                Text(text = "Guardar un nuevo registro", fontSize = 24.sp)
            }
        }
    }
}


package com.lubaspc.traveltolucos

import android.graphics.Movie
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lubaspc.traveltolucos.model.*
import com.lubaspc.traveltolucos.room.ChargePersonDb
import com.lubaspc.traveltolucos.room.DBRoom
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.room.TypeOperation
import com.lubaspc.traveltolucos.service.GenericResponse
import com.lubaspc.traveltolucos.service.RetrofitService
import com.lubaspc.traveltolucos.service.model.Movimiento
import com.lubaspc.traveltolucos.service.model.Usuario
import com.lubaspc.traveltolucos.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MTViewModel : ViewModel() {
    private val dao by lazy {
        DBRoom.db.dbDao()
    }
    private val repository by lazy {
        RetrofitService()
    }

    val dateSelected = mutableStateOf(Calendar.getInstance())
    val charges = mutableStateListOf<ChargeMD>()
    val persons = mutableStateListOf<PersonMD>()
    val dayChanger = mutableStateListOf<ChargeDayMD>()
    val history = mutableStateListOf<ChargeDayMD>()
    val weeks = mutableStateListOf<WeekModel>()
    val saved = mutableStateOf(false)

    val accountData = MutableLiveData<Usuario>()
    val movements = MutableLiveData<List<Movimiento>>()

    init {
        setDateSelect(dateSelected.value)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAccount()
            if (response.errorCode in 200..299) {
                accountData.postValue(response.data)
                movements.postValue(
                    response.data?.tags?.map {
                        repository.getMovements(it,dateSelected.value)
                    }?.flatMap {
                        it.data ?: listOf() }
                )
            }
        }
    }

    fun setDateSelect(it: Calendar) {
        dateSelected.value = it

        dao.chargesDay(it).onEach { list ->
            dayChanger.clear()
            dayChanger.addAll(list.map { d ->
                d.chargePerson.run {
                    ChargeDayMD(
                        chargeIdFk,
                        personIdFk,
                        day,
                        total,
                        payment,
                        pay,
                        d.charge.toMd,
                        d.person.toMd,
                    )
                }
            })
            getCharges()
            getPerson()
        }.launchIn(viewModelScope)
    }

    fun consultHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            history.clear()
            history.addAll(dao.getDays().map { d ->
                d.chargePerson.run {
                    ChargeDayMD(
                        chargeIdFk,
                        personIdFk,
                        day,
                        total,
                        payment,
                        pay,
                        d.charge.toMd,
                        d.person.toMd,
                    )
                }
            })

            /*weeks.addAll(
                history.mapIndexed { i, histori ->
                    histori.day.moveField(Calendar.SUNDAY).run {
                        val monday = moveField(Calendar.MONDAY, true)
                        val sunday = moveField(Calendar.SUNDAY, next = true, addOne = true)
                        val days = history.filter { h -> h.day.into(monday, sunday) }
                        val persons = history.groupBy { it.person }
                        WeekModel(
                            monday,
                            sunday,
                            days.sumOf { h -> h.payment },
                            mutableStateOf(i > history.size - 1),
                            mutableStateOf(!persons.values.any { it.any { !it.pay } }),
                            persons.map { group ->
                                val daysPerson = group.value.distinctBy { h -> h.day.parseDate() }
                                PersonModel(
                                    group.key,
                                    daysPerson.sumOf { h -> h.total },
                                    daysPerson.sortedBy { it.day },
                                    mutableStateOf(daysPerson.firstOrNull()?.pay != true)
                                )
                            },
                            days
                        )
                    }
                }.distinctBy { h -> h.monday }
                    .sortedByDescending { h -> h.monday }


            )*/
        }
    }

    private fun getPerson() {
        dao.getPerson().onEach { list ->
            persons.clear()
            persons.addAll(list.map {
                it.toMd.apply {
                    checked.value = dayChanger.any { day -> day.personIdFk == it.personId }
                }
            })
        }.launchIn(viewModelScope)
    }


    private fun getCharges() {
        dao.getCharges().onEach { list ->
            charges.clear()
            charges.addAll(list.map {
                it.toMd.apply {
                    checked.value = dayChanger.any { day -> day.chargeIdFk == it.chargeId }
                }
            })
        }.launchIn(viewModelScope)
    }

    fun saveForm() {
        val personsSize = persons.filter { it.checked.value }.size + 1
        viewModelScope.launch(Dispatchers.IO) {
            persons.filter { it.checked.value }.forEach { p ->
                val total = p.listCharges.sumOf { c ->
                    if (c.checked.value) {
                        c.total.value / (if (c.type == TypeCharge.GROUP) personsSize
                        else 1) * (if (c.operation == TypeOperation.MINUS) -1 else 1)
                    } else 0.0
                }
                p.listCharges.filter { it.checked.value }.forEach { c ->
                    dao.insertDay(
                        ChargePersonDb(
                            chargeIdFk = c.id,
                            personIdFk = p.personId,
                            day = dateSelected.value,
                            total = total,
                            payment = if (c.checked.value) {
                                c.total.value / (if (c.type == TypeCharge.GROUP) personsSize
                                else 1) * (if (c.operation == TypeOperation.MINUS) -1 else 1)
                            } else 0.0,
                            pay = false
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                saved.value = true
            }
        }
    }

    fun confirmPay(days: List<ChargeDayMD>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateDay(*days.map {
                ChargePersonDb(it.chargeIdFk, it.personIdFk, it.day, it.total, it.payment, true)
            }.toTypedArray())
        }
    }

}
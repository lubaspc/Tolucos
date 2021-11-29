package com.lubaspc.traveltolucos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.model.ChargeMD
import com.lubaspc.traveltolucos.model.PersonMD
import com.lubaspc.traveltolucos.room.ChargePersonDb
import com.lubaspc.traveltolucos.room.DBRoom
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.room.TypeOperation
import com.lubaspc.traveltolucos.utils.parseDate
import com.lubaspc.traveltolucos.utils.toMd
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

    val dateSelected = mutableStateOf(Calendar.getInstance())
    val charges = mutableStateListOf<ChargeMD>()
    val persons = mutableStateListOf<PersonMD>()
    val dayChanger = mutableStateListOf<ChargeDayMD>()
    val history = mutableStateListOf<ChargeDayMD>()
    val saved = mutableStateOf(false)

    init {
        setDateSelect(dateSelected.value)
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

    fun getHistory(monday: Calendar, friday: Calendar) {
        dao.getDays(monday, friday).onEach {
            history.clear()
            history.addAll(it.map { d ->
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
        }.launchIn(viewModelScope)
    }

    fun consultHistory() {
        dao.getDays().onEach {
            history.clear()
            history.addAll(it.map { d ->
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
        }.launchIn(viewModelScope)
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
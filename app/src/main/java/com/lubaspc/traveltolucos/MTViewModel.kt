package com.lubaspc.traveltolucos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lubaspc.traveltolucos.model.*
import com.lubaspc.traveltolucos.room.ChargePersonDb
import com.lubaspc.traveltolucos.room.DBRoom
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.service.RetrofitService
import com.lubaspc.traveltolucos.service.model.Movimiento
import com.lubaspc.traveltolucos.service.model.Tag
import com.lubaspc.traveltolucos.service.model.Usuario
import com.lubaspc.traveltolucos.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MTViewModel : ViewModel() {
    private val dao by lazy {
        DBRoom.db.dbDao()
    }
    private val repository by lazy {
        RetrofitService()
    }

    val dateSelected = MutableLiveData(Calendar.getInstance())
    val charges = MutableLiveData<MutableList<ChargeMD>>()
    val persons = MutableLiveData<List<PersonMD>>()
    val personSave = MutableLiveData<List<PersonMD>>()
    val dayChanger = MutableLiveData<List<ChargeDayMD>>()
    val weeks = MutableLiveData<List<WeekModel>>()
    val movements = MutableLiveData<List<Movimiento>>()

    val accountData = MutableLiveData<Usuario?>()

    init {
        dateSelected.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                dayChanger.postValue(dao.chargesDay(it).map { d ->
                    d.chargePerson.run {
                        ChargeDayMD(
                            idChargePerson,
                            personIdFk,
                            day,
                            total,
                            payment,
                            pay,
                            d.chargePerson.description,
                            d.person.toMd,
                            noPersons,
                        )
                    }
                })
                getCharges()
                getPerson()
                getMovements(accountData.value?.tags, it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            accountData.postValue(repository.getAccount().data.also {
                getMovements(it?.tags, dateSelected.value!!)
            })
        }
    }

    private suspend fun getMovements(tags: List<Tag>?, date: Calendar) {
        if (tags == null) return
        movements.postValue(tags.map {
            repository.getMovements(it, date)
        }.flatMap {
            it.data ?: listOf()
        }.also {
            charges.postValue(charges.value?.apply {
                addAll(0,
                    it.filter { it.fecha.parseDate() == date.parseDate() }
                        .map {
                            ChargeMD(
                                0,
                                it.caseta + " " + it.tramo,
                                it.monto,
                                1,
                                TypeCharge.GROUP,
                                it.monto,
                                true
                            )
                        })
            })
        })
    }


    fun consultHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = dao.getDays()
            weeks.postValue(
                history.mapIndexed { i, d ->
                    d.chargePerson.day.moveField(Calendar.SUNDAY).run {
                        val monday = moveField(Calendar.MONDAY, true)
                        val sunday = moveField(Calendar.SUNDAY, next = true, addOne = true)
                        val days = history.filter { h -> h.chargePerson.day.into(monday, sunday) }
                        val persons = history.groupBy { it.person }
                        WeekModel(
                            monday,
                            sunday,
                            days.sumOf { h -> h.chargePerson.payment },
                            i > history.size - 1,
                            !persons.values.any { it.any { !it.chargePerson.pay } },
                            persons.map { group ->
                                val daysPerson =
                                    group.value.distinctBy { h -> h.chargePerson.day.parseDate() }
                                PersonModel(
                                    group.key.name,
                                    daysPerson.sumOf { h -> h.chargePerson.total },
                                    daysPerson.map { dp ->
                                        val personSize =
                                            history.filter { h -> h.chargePerson.day == dp.chargePerson.day }
                                                .distinctBy { it.person }.size
                                        DayModel(
                                            dp.chargePerson.day,
                                            dp.chargePerson.total,
                                            personSize,
                                            daysPerson.filter { it.chargePerson.day.parseDate() == dp.chargePerson.day.parseDate() }
                                                .map {
                                                    ChargeModel(
                                                        it.chargePerson.description,
                                                        it.chargePerson.total,
                                                        it.chargePerson.payment
                                                    )
                                                }
                                        )
                                    },
                                    daysPerson.firstOrNull()?.chargePerson?.pay != true
                                )
                            },
                        )
                    }
                }.distinctBy { it.monday }
                    .sortedByDescending { it.monday }
            )
        }
    }

    private fun getPerson() {
        viewModelScope.launch(Dispatchers.IO) {
            persons.postValue(dao.getPerson().map {
                it.toMd.apply {
                    checked = dayChanger.value?.any { day -> day.personIdFk == it.personId } == true
                }
            })
        }

    }


    private fun getCharges() {
        viewModelScope.launch(Dispatchers.IO) {
            charges.postValue(dao.getCharges().map {
                it.toMd.apply {
                    checked = dayChanger.value?.any { day -> day.chargeId == it.chargeId } == true
                }
            }.toMutableList())
        }
    }

    fun saveForm() {
        val personsSize = personSave.value?.size ?: 0 + 1
        viewModelScope.launch(Dispatchers.IO) {
            fun ChargeMD.getTotal() =
                if (checked) total / (if (type == TypeCharge.GROUP) personsSize else 1)
                else 0.0
            personSave.value?.forEach { p ->
                p.listCharges.filter { it.checked }.forEach { c ->
                    val data = ChargePersonDb(
                        personIdFk = p.personId, //Correco
                        day = dateSelected.value!!, //Correcto
                        total = c.total,
                        description = c.description,
                        payment = c.getTotal(),
                        pay = false,
                        chargeId = c.id,
                        noPersons = if (c.type == TypeCharge.GROUP) personsSize else 1
                    )
                    dao.insertDay(data)
                }
            }
        }
    }

    fun confirmPay(days: List<ChargeDayMD>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateDay(*days.map {
                ChargePersonDb(
                    it.idChargePerson,
                    it.personIdFk,
                    it.description,
                    it.day,
                    it.total,
                    it.payment,
                    it.noPersons,
                    true
                )
            }.toTypedArray())
        }
    }

    fun createSaveFrom(chargesDay: List<ChargeMD>) {
        personSave.value =
            persons.value?.filter { p -> p.checked }?.map { p ->
                p.copy().apply {
                    p.listCharges.clear()
                    p.listCharges.addAll(chargesDay.map { c -> c.copy() })
                    p.listCharges.addAll(
                        charges.value?.filter { it.type == TypeCharge.PERSONAL } ?: listOf()
                    )
                }
            }
    }

}
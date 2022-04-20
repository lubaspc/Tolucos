package com.lubaspc.traveltolucos

import android.util.Log
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
import com.lubaspc.traveltolucos.service.telegramBot.BotRepository
import com.lubaspc.traveltolucos.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

import androidmads.library.qrgenearator.QRGContents

import androidmads.library.qrgenearator.QRGEncoder
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.lubaspc.traveltolucos.service.GasRepository

import java.io.File

class MTViewModel : ViewModel() {
    private val dao by lazy {
        DBRoom.db.dbDao()
    }
    private val repository by lazy {
        RetrofitService()
    }
    private val repositoryBotT by lazy {
        BotRepository()
    }

    private val repositoryGas by lazy {
        GasRepository()
    }


    val showProgress = MutableLiveData(false)
    val dateSelected = MutableLiveData(Calendar.getInstance())
    val daysExist = MutableLiveData<List<Calendar>>()
    val charges = MutableLiveData<MutableList<ChargeMD>>()
    val persons = MutableLiveData<List<PersonMD>>()
    val personSave = MutableLiveData<List<PersonMD>>()
    val dayChanger = MutableLiveData<List<ChargeDayMD>>()
    val weeks = MutableLiveData<List<WeekModel>>()
    val movements = MutableLiveData<List<Movimiento>>()

    val accountData = MutableLiveData<Usuario?>()
    val priceGas = MutableLiveData<Double>()

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
        getAccountData()
    }

    fun getAccountData() {
        viewModelScope.launch(Dispatchers.IO) {
            accountData.postValue(repository.getAccount().data.also {
                getMovements(it?.tags, dateSelected.value!!)
            })
        }
    }

    fun getPriceGas() {
        viewModelScope.launch(Dispatchers.IO) {
            val newPrice = repositoryGas.getPricePremium()
            if (newPrice != null)
                dao.updatePriceGas(newPrice)
            priceGas.postValue(
                newPrice ?: charges.value?.first { it.id == 9L }?.total ?: 25.0
            )
        }
    }

    private suspend fun getMovements(tags: List<Tag>?, date: Calendar) {
        if (tags == null) return
        movements.postValue(tags.map {
            repository.getMovements(it, date)
        }.flatMap {
            it.data ?: listOf()
        }.also {
            charges.postValue(
                charges.value?.filter { it.id != 0L }
                    ?.toMutableList()?.apply {
                        addAll(0,
                            it.filter { it.fecha.parseDate() == date.parseDate() }
                                .map {
                                    ChargeMD(
                                        0,
                                        it.tramo,
                                        it.monto,
                                        1,
                                        TypeCharge.GROUP,
                                        true
                                    )
                                })
                    })
        })
    }


    fun consultHistory() {
        showProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val history = dao.getDays()
            weeks.postValue(
                history.distinctBy { it.chargePerson.noWeek }
                    .mapIndexed { i, d ->
                        d.chargePerson.day.moveField(Calendar.SUNDAY).run {
                            val monday = moveField(Calendar.MONDAY, true)
                            val sunday = moveField(Calendar.SUNDAY, next = true, addOne = true)
                            val days =
                                history.filter { h -> h.chargePerson.day.into(monday, sunday) }
                            val persons = days.groupBy { it.person }
                            WeekModel(
                                monday,
                                sunday,
                                days.sumOf { h -> h.chargePerson.payment },
                                i > history.size - 1,
                                !persons.values.any { it.any { !it.chargePerson.pay } },
                                persons.map { group ->
                                    val daysPerson =
                                        days.filter { it.person.name == group.key.name }
                                    if (group.key.name == "Charly") {
                                        daysPerson.any { !it.chargePerson.pay }
                                    }
                                    PersonModel(
                                        group.key.name,
                                        group.key.phone,
                                        daysPerson.sumOf { h -> h.chargePerson.payment },
                                        !daysPerson.any { it.chargePerson.pay },
                                        daysPerson.any { it.chargePerson.pay },
                                        daysPerson.distinctBy { it.chargePerson.day.parseDate() }
                                            .map { dp ->
                                                val daysPersonFilter =
                                                    daysPerson.filter { it.chargePerson.day.parseDate() == dp.chargePerson.day.parseDate() }
                                                DayModel(
                                                    dp.chargePerson.day,
                                                    daysPersonFilter.sumOf { it.chargePerson.payment },
                                                    dp.chargePerson.noPersons,
                                                    daysPersonFilter.map {
                                                        ChargeModel(
                                                            it.chargePerson.idChargePerson,
                                                            it.chargePerson.idMessage,
                                                            it.chargePerson.description,
                                                            it.chargePerson.total,
                                                            it.chargePerson.payment
                                                        )
                                                    }
                                                )
                                            },
                                    )
                                },
                            )
                        }
                    }
                    .sortedByDescending { it.monday }
            )
            showProgress.postValue(false)
        }
    }

    fun getDaysRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            daysExist.postValue(dao.getDisDays())
        }
    }

    private fun getPerson() {
        viewModelScope.launch(Dispatchers.IO) {
            persons.postValue(dao.getPerson().map { it.toMd })
        }

    }


    private fun getCharges() {
        viewModelScope.launch(Dispatchers.IO) {
            charges.postValue(dao.getCharges()
                .map { it.toMd }
                .toMutableList())
        }
    }

    fun saveForm() {
        val personsSize = (personSave.value?.size ?: 0) + 1
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
                        noPersons = if (c.type == TypeCharge.GROUP) personsSize else 1,
                        noWeek = dateSelected.value!!.get(Calendar.WEEK_OF_YEAR)
                    )
                    dao.insertDay(data)
                }
            }
        }
    }

    fun confirmPay(person: PersonModel, imgPay: File) {
        showProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            person.days.flatMap { it.charges }.distinctBy { it.idChargePerson }.forEach {
                dao.updateChargePerson(true, it.idChargePerson)
            }
            val messagePay = person.messageWeek(true)
            person.days.flatMap { it.charges.map { it.idMessage } }.distinct()
                .forEach {
                    if (it == null) return@forEach
                    repositoryBotT.editMessage(imgPay, messagePay, it)
                }
            showProgress.postValue(false)
            consultHistory()
        }
    }

    fun createSaveFrom(chargesDay: List<ChargeMD>) {
        personSave.value =
            persons.value?.filter { p -> p.checked }?.map { p ->
                p.copy().apply {
                    p.listCharges.clear()
                    p.listCharges.addAll(chargesDay.map { c -> c.copy() })
                    p.listCharges.addAll(
                        charges.value?.filter { it.type == TypeCharge.PERSONAL }?.map { it.copy() }
                            ?: listOf()
                    )
                }
            }
    }

    fun removeDay(dayRemoveSelect: Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteChargePerson(dayRemoveSelect)
        }
    }

    fun consultPlusWeek() {

    }

    //  "<a href=\"aztecapay://?$qr/\">Banco Azteca</a>\n\n"
    //  "<a href=\"baz://send_money/$qr/\">Baz</a>"
    fun sendMessage(vararg persons: PersonModel) {
        viewModelScope.launch(Dispatchers.IO) {
            showProgress.postValue(true)
            try {
                persons.forEach { p ->
                    var weekPerson = p.messageWeek()
                    val weeksDeuda =
                        weeks.value?.flatMap { w -> w.persons.filter { it.person == p.person && !it.completePay && it != p } }
                    if (!weeksDeuda.isNullOrEmpty()) {
                        weekPerson += "\n\n<b>Montos de semanas anteriores</b>\n${
                            weeksDeuda.joinToString("\n") { "<b>-> ${it.total.formatPrice}</b>" }
                        }"
                    }
                    val total = p.total + (weeksDeuda?.sumOf { it.total } ?: 0.0)
                    val qr = generateQR(total)
                    val responseImage = repositoryBotT.sendPhoto(
                        QRGEncoder(
                            qr,
                            null,
                            QRGContents.Type.TEXT,
                            720
                        ).encodeAsBitmap()
                            .convertToFile(),
                        weekPerson + "\n\nTotal del QR: ${total.formatPrice}"
                        // + "\n<a href=\"https://digital.bienestarazteca.com/pagoqr/$qr\">Pago con Baz</a>"
                    )
                    if (responseImage.data?.ok == false) {
                        return@forEach
                    }
                    val idCharges = p.days.flatMap { it.charges.map { it.idChargePerson } }
                    dao.saveMessageId(
                        responseImage.data?.result?.messageId,
                        idCharges
                    )
                }
            } catch (e: Exception) {
                throw e
            } finally {
                consultHistory()
                showProgress.postValue(false)
            }
        }
    }

}
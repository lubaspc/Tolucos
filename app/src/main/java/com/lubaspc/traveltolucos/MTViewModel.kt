package com.lubaspc.traveltolucos

import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.util.Pair
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lubaspc.traveltolucos.model.*
import com.lubaspc.traveltolucos.room.ChargePersonDb
import com.lubaspc.traveltolucos.room.DBRoom
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.service.GasRepository
import com.lubaspc.traveltolucos.service.RetrofitService
import com.lubaspc.traveltolucos.service.model.Movimiento
import com.lubaspc.traveltolucos.service.model.Tag
import com.lubaspc.traveltolucos.service.model.Usuario
import com.lubaspc.traveltolucos.service.prometeo.PrometeoRepository
import com.lubaspc.traveltolucos.service.whatsappcloud.WhatsappCloudRepository
import com.lubaspc.traveltolucos.service.whatsappcloud.models.MessageRequest
import com.lubaspc.traveltolucos.service.whatsappcloud.models.TemplanesEnum
import com.lubaspc.traveltolucos.service.whatsappcloud.models.TypeComponentEnum
import com.lubaspc.traveltolucos.service.whatsappcloud.models.TypeMessageEnum
import com.lubaspc.traveltolucos.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MTViewModel : ViewModel() {
    private val dao by lazy {
        DBRoom.db.dbDao()
    }
    private val repository by lazy {
        RetrofitService()
    }

    private val repositoryGas by lazy {
        GasRepository()
    }

    private val repositoryWhatsapp by lazy {
        WhatsappCloudRepository()
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
    val movementsConsult = MutableLiveData<List<Movimiento>>()

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

    private suspend fun getMovements(tags: List<Tag>?, date: Calendar) {
        if (tags == null) return
        movements.postValue(tags.map {
            repository.getMovements(it, date)
        }.flatMap {
            it.data ?: listOf()
        }.also {
            val priceGas = repositoryGas.getPricePremium()
            charges.postValue(
                charges.value?.filter { it.id != 0L }
                    ?.toMutableList()?.apply {
                        addAll(0,
                            it.filter { it.fecha.parseDate() == date.parseDate() }
                                .map {
                                    ChargeMD(
                                        0,
                                        "${it.fecha.parseDate("hh:ss a")} ${it.tramo} ${it.caseta}",
                                        it.monto,
                                        1,
                                        TypeCharge.GROUP,
                                        true
                                    )
                                })
                        add(
                            ChargeMD(
                                98,
                                "1/2 Consumo Gasolina",
                                (priceGas ?: 22.9) / 2,
                                15,
                                TypeCharge.GROUP,
                                true
                            )
                        )
                        add(
                            ChargeMD(
                                99,
                                "2/2 Consumo Gasolina",
                                (priceGas ?: 22.9) / 2,
                                15,
                                TypeCharge.GROUP,
                                true
                            )
                        )
                    })
        })
    }

    fun getMovements(rangeDate: Pair<Long, Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            movementsConsult.postValue(accountData.value?.tags?.flatMap {
                repository.getMovements(
                    it,
                    Date(rangeDate.first).parseDate("YYYY-MM-dd"),
                    Date(rangeDate.second).parseDate("YYYY-MM-dd")
                ).data ?: listOf()
            })
        }

    }

    fun getProviders() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                "PROVIDE_S",
                PrometeoRepository().login().data?.status ?: "NO FAIDED"
            )

        }
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
                            Log.d("TOLUCAOS_NEW", "${monday.parseDate()} ${sunday.parseDate()}")
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
            consultHistory()
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
                    //repositoryBotT.editMessage(imgPay, messagePay, it)
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
                    var weekPerson = p.messageWeekWhatsapp()
                    val weeksDeuda =
                        weeks.value?.flatMap { w -> w.persons.filter { it.person == p.person && !it.completePay && it != p } }
                    val total = p.total + (weeksDeuda?.sumOf { it.total } ?: 0.0)
                    val qr = generateQR(total)

                    val qrResponse = repositoryWhatsapp.uploadImage(
                        QRGEncoder(
                            qr,
                            null,
                            QRGContents.Type.TEXT,
                            720
                        ).encodeAsBitmap()
                            .convertToFile()
                    )
                    val messageResponse = repositoryWhatsapp.sendMessage(
                        MessageRequest(
                            to = p.phone,
                            type = TypeMessageEnum.TEMPLATE,
                            template = MessageRequest.Template(
                                name = TemplanesEnum.REQUEST_PAYMENT.templateName,
                                components = listOf(
                                    MessageRequest.Template.Component(
                                        type = TypeComponentEnum.HEADER,
                                        parameters = listOf(
                                            MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.IMAGE,
                                                image = MessageRequest.ImageMessage(
                                                    id = qrResponse.data?.id
                                                )
                                            )
                                        )
                                    ),
                                    MessageRequest.Template.Component(
                                        type = TypeComponentEnum.BODY,
                                        parameters = listOf(
                                            MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = p.days.first().day.get(Calendar.WEEK_OF_YEAR)
                                                    .toString()
                                            ),
                                            MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = p.days.joinToString(",") {
                                                    "_${it.day.parseDate("dd-MMM")}:_ ${it.total.formatPrice}"
                                                },
                                            ),
                                            MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = p.total.formatPrice
                                            ), MessageRequest.Template .Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = if (weeksDeuda.isNullOrEmpty()) "Sin Deudas" else "_${weeksDeuda.size}_: ${weeksDeuda.sumOf { it.total }.formatPrice}"
                                            ), MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = total.formatPrice
                                            )
                                        )
                                    ),
                                    MessageRequest.Template.Component(
                                        type = TypeComponentEnum.BUTTON,
                                        subType = "url",
                                        index = 0,
                                        parameters = listOf(
                                            MessageRequest.Template.Component.Parameter(
                                                type = TypeMessageEnum.TEXT,
                                                text = qr.replace("\n","")
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )

//                    val responseImage = repositoryBotT.sendPhoto(
//                        QRGEncoder(
//                            qr,
//                            null,
//                            QRGContents.Type.TEXT,
//                            720
//                        ).encodeAsBitmap()
//                            .convertToFile(),
//                        weekPerson + "\n\nTotal del QR: ${total.formatPrice}" +
//                                "\n<a href=\"https://www.bienestarazteca.com.mx/pagoqr/?IdApp=2&qrData=$qr\">Pago con Baz</a>"
//                    )
//                    if (responseImage.data?.ok == false) {
//                        return@forEach
//                    }
                    val idCharges = p.days.flatMap { it.charges.map { it.idChargePerson } }
                    dao.saveMessageId(
                        "${messageResponse.data?.messages?.firstOrNull()?.id},${qrResponse.data?.id}",
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
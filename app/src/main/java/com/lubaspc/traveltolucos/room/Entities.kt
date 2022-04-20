package com.lubaspc.traveltolucos.room

import androidx.room.*
import java.util.*


enum class TypeCharge{
    PERSONAL,GROUP
}

//Tables
@Entity
data class ChargeDb(
    @PrimaryKey
    var chargeId: Long,
    var description: String,
    var price: Double, //CU
    var amount: Int, //Cantidad
    var total: Double,//Total
    var type: TypeCharge,
)

@Entity
data class PersonDb(
    @PrimaryKey
    var personId: Long,
    var name: String,
    var phone: String,

)

//Relations
@Entity
data class ChargePersonDb(
    @PrimaryKey(autoGenerate = true)
    var idChargePerson: Long = 0,
    var personIdFk: Long, //Persona
    var description: String, // Charge
    var day: Calendar,// Dia
    var noWeek: Int,
    var total: Double, // Cobro del cargo en total
    var payment: Double, // Cobro de esta persona a este cargo
    var noPersons: Int ,// A cuantos se les cargo esto
    var pay: Boolean, // Esta pagado
    var chargeId: Long = 0, // Si lo hay,
    var idMessage: String? = null
)

//Embedded
data class DayRelationDb(
    @Embedded val chargePerson: ChargePersonDb,
    @Relation(
        parentColumn = "personIdFk",
        entityColumn = "personId",
    )
    val person: PersonDb
)
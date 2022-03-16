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
    var price: Double,
    var amount: Int,
    var total: Double,
    var type: TypeCharge,
)

@Entity
data class PersonDb(
    @PrimaryKey
    var personId: Long,
    var name: String,
    var phone: String
)

//Relations
@Entity()
data class ChargePersonDb(
    @PrimaryKey(autoGenerate = true)
    var idChargePerson: Long = 0,
    var personIdFk: Long,
    var description: String,
    var day: Calendar,
    var total: Double,
    var payment: Double,
    var pay: Boolean
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
package com.lubaspc.traveltolucos.room

import androidx.room.*
import java.util.*


enum class TypeCharge{
    PERSONAL,GROUP
}

enum class TypeOperation{
    MINUS,PLUS
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
    var operation: TypeOperation,

)

@Entity
data class PersonDb(
    @PrimaryKey
    var personId: Long,
    var name: String,
    var phone: String
)

//Relations
@Entity(primaryKeys = ["chargeIdFk", "personIdFk","day"])
data class ChargePersonDb(
    var chargeIdFk: Long,
    var personIdFk: Long,
    var day: Calendar,
    var total: Double,
    var payment: Double,
    var pay: Boolean
)

//Embedded
data class DayRelationDb(
    @Embedded val chargePerson: ChargePersonDb,
    @Relation(
        parentColumn = "chargeIdFk",
        entityColumn = "chargeId",
    )
    val charge: ChargeDb,
    @Relation(
        parentColumn = "personIdFk",
        entityColumn = "personId",
    )
    val person: PersonDb
)

data class PersonWhitCharges(
    @Embedded val person: PersonDb,
    @Relation(
        parentColumn = "id",
        entityColumn = "chargeId",
        associateBy = Junction(ChargePersonDb::class)
    )
    val charges: List<ChargeDb>,
)

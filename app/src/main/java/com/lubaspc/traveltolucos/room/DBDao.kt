package com.lubaspc.traveltolucos.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DBDao{

    @Query("select * from ChargePersonDb where day = :day")
    suspend fun chargesDay(day:Calendar): List<DayRelationDb>

    @Query("select * from ChargePersonDb ORDER BY day DESC")
    suspend fun getDays(): List<DayRelationDb>

    @Query("update ChargeDb set price = :price where chargeId = 9")
    suspend fun updatePriceGas(price: Double)

    @Query("DELETE FROM ChargeDb where chargeId = 9")
    suspend fun deleteGas()

    @Query("select * from ChargePersonDb where day BETWEEN :monday and :friday")
    suspend fun getDays(monday: Calendar,friday: Calendar): List<DayRelationDb>

    @Query("select DISTINCT day from ChargePersonDb")
    suspend fun getDisDays(): List<Calendar>

    @Query("select * from ChargeDb")
    suspend fun getCharges() : List<ChargeDb>

    @Query("select * from PersonDb")
    suspend fun getPerson() : List<PersonDb>

    @Insert(onConflict = REPLACE)
    suspend fun insertCharge(charge: ChargeDb)

    @Insert(onConflict = REPLACE)
    suspend fun insertPerson(person: PersonDb)

    @Insert(onConflict = REPLACE)
    suspend fun insertDay(vararg day: ChargePersonDb)

    @Update(onConflict = REPLACE)
    suspend fun updateDay(vararg day: ChargePersonDb)

    @Query("update ChargePersonDb set pay = :pay where idChargePerson = :id")
    fun updateChargePerson(pay: Boolean,id: Long)

    @Query("update ChargePersonDb set idMessage = :idMessage where idChargePerson in (:ids)")
    fun saveMessageId(idMessage: String?,ids: List<Long>)

    @Query("DELETE FROM ChargePersonDb")
    fun deleteChargePerson()

    @Query("DELETE FROM ChargePersonDb where day = :day")
    fun deleteChargePerson(day: Calendar)
}

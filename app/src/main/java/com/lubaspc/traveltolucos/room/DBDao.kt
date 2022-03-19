package com.lubaspc.traveltolucos.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DBDao{

    @Query("select * from ChargePersonDb where day = :day")
    suspend fun chargesDay(day:Calendar): List<DayRelationDb>

    @Query("select * from ChargePersonDb")
    suspend fun getDays(): List<DayRelationDb>


    @Query("select * from ChargePersonDb where day BETWEEN :monday and :friday")
    suspend fun getDays(monday: Calendar,friday: Calendar): List<DayRelationDb>

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

    @Query("DELETE FROM ChargePersonDb")
    fun deleteChargePerson()
}

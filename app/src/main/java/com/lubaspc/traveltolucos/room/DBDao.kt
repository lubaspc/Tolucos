package com.lubaspc.traveltolucos.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DBDao{

    @Query("select * from ChargePersonDb where day = :day")
    fun chargesDay(day:Calendar): Flow<List<DayRelationDb>>

    @Query("select * from ChargePersonDb")
    fun getDays(): Flow<List<DayRelationDb>>


    @Query("select * from ChargePersonDb where day BETWEEN :monday and :friday")
    fun getDays(monday: Calendar,friday: Calendar): Flow<List<DayRelationDb>>

    @Query("select * from ChargeDb")
    fun getCharges() : Flow<List<ChargeDb>>

    @Query("select * from PersonDb")
    fun getPerson() : Flow<List<PersonDb>>

    @Insert(onConflict = REPLACE)
    fun insertCharge(charge: ChargeDb)

    @Insert(onConflict = REPLACE)
    fun insertPerson(person: PersonDb)

    @Insert(onConflict = REPLACE)
    fun insertDay(vararg day: ChargePersonDb)

    @Update(onConflict = REPLACE)
    fun updateDay(vararg day: ChargePersonDb)

}

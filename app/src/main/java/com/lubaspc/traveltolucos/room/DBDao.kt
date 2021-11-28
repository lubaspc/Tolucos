package com.lubaspc.traveltolucos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DBDao{

    @Query("select * from ChargePersonDb where day = :day")
    fun chargesDay(day:Calendar): Flow<List<DayRelationDb>>

    @Query("select * from ChargePersonDb")
    fun getDays(): Flow<List<DayRelationDb>>

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

}

package com.lubaspc.traveltolucos.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ChargeDb::class,
        PersonDb::class,
        ChargePersonDb::class
    ],
    version = 3,
)
@TypeConverters(Converters::class)
abstract class DBRoom : RoomDatabase() {
    companion object {
        lateinit var db: DBRoom
    }

    abstract fun dbDao(): DBDao
}
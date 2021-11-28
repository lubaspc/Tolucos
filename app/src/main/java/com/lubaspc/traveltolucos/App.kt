package com.lubaspc.traveltolucos

import android.app.Application
import androidx.room.Room
import com.lubaspc.traveltolucos.room.DBRoom

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DBRoom.db = Room.databaseBuilder(
            applicationContext,
            DBRoom::class.java,
            "db-tolucos"
        ).build()
    }
}
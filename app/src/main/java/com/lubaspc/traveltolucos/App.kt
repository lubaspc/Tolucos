package com.lubaspc.traveltolucos

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.lubaspc.traveltolucos.room.DBRoom
import java.io.File

class App : Application() {
    companion object {
        lateinit var sharedPreferences: SharedPreferences
        const val COOKIES = "COOKIES"

    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("Travels", Context.MODE_PRIVATE)
        DBRoom.db = Room.databaseBuilder(
            applicationContext,
            DBRoom::class.java,
            "db-toluca"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
package com.lubaspc.traveltolucos.room

import androidx.room.TypeConverter
import com.lubaspc.traveltolucos.utils.parseCalendar
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String) = value.parseCalendar()

    @TypeConverter
    fun dateToTimestamp(date: Calendar) = date.parseDate()
}


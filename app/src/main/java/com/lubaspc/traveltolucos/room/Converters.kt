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

    @TypeConverter
    fun toTypeCharge(value: String) = enumValueOf<TypeCharge>(value)

    @TypeConverter
    fun fromTypeCharge(value: TypeCharge) = value.name

    @TypeConverter
    fun toTypeOperation(value: String) = enumValueOf<TypeOperation>(value)

    @TypeConverter
    fun fromTypeOperation(value: TypeOperation) = value.name
}


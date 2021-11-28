package com.lubaspc.traveltolucos.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.parseDate(format: String = "dd/MM/yyyy"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(time)

fun String.parseCalendar(format: String = "dd/MM/yyyy"): Calendar =
    Calendar.getInstance().apply {
        time = SimpleDateFormat(format, Locale.getDefault()).parse(this@parseCalendar) ?: Date()
    }

fun Date.parseDate(format: String = "dd/MM/yyyy"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

fun String.parseDate(format: String = "dd/MM/yyyy"): Date? =
    SimpleDateFormat(format, Locale.getDefault()).parse(this)


fun Calendar.moveField(field: Int,next: Boolean = false): Calendar{
    val newCalendar = Calendar.getInstance().apply {
        timeInMillis = this@moveField.timeInMillis
    }
    while (newCalendar.get(Calendar.DAY_OF_WEEK) != field) {
        newCalendar.add(
            Calendar.DAY_OF_YEAR,
            if (next) 1 else -1
        )
    }
    return newCalendar
}

fun Calendar.into(firs: Calendar,second: Calendar) =
    timeInMillis >= firs.timeInMillis && timeInMillis <= second.timeInMillis
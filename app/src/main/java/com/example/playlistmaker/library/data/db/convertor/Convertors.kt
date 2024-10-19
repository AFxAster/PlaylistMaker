package com.example.playlistmaker.library.data.db.convertor

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {

    @TypeConverter
    fun dateFromString(value: String): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value)!!
    }

    @TypeConverter
    fun stringFromDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }
}
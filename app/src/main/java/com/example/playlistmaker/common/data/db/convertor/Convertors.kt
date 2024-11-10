package com.example.playlistmaker.common.data.db.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Converters {
    private val listType: Type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun dateFromString(value: String): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value)!!
    }

    @TypeConverter
    fun stringFromDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    @TypeConverter
    fun stringFromList(list: List<String>): String = Gson().toJson(list, listType)


    @TypeConverter
    fun listFromString(str: String): List<String> = Gson().fromJson(str, listType)

}
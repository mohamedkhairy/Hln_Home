package com.example.hlnhome.converter

import androidx.room.TypeConverter
import com.example.hlnhome.database.entity.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun fromString(value: String): List<Service>? {
        val listType = object : TypeToken<List<Service>>() {}.type
        return Gson().fromJson<List<Service>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Service>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
package com.h0uss.aimart.data.converter

import androidx.room.TypeConverter

open class ListStringConverter {
    @TypeConverter
    fun fromListString(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListString(data: String?): List<String>? {
        if (data.isNullOrBlank()) {
            return emptyList()
        }

        return data.split(",")
    }
}
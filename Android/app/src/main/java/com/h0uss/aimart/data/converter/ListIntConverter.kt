package com.h0uss.aimart.data.converter

import androidx.room.TypeConverter

open class ListIntConverter {
    @TypeConverter
    fun fromListInt(list: List<Int>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListInt(data: String?): List<Int>? {
        if (data.isNullOrBlank()) {
            return emptyList()
        }

        return data.split(",")
            .mapNotNull {
                it.toIntOrNull()
            }
    }
}
package com.h0uss.aimart.data.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

open class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return if (value.isNullOrEmpty()) {
            null
        } else {
            value.toBigDecimalOrNull()
        }
    }
}
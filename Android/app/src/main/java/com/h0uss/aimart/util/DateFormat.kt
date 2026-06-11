package com.h0uss.aimart.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SelectableDates
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
val customFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd.MM.yyyy")

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, customFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    return this.toLocalDate().atStartOfDay()
}

@RequiresApi(Build.VERSION_CODES.O)
object DateValidatorNoFuture: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}

@RequiresApi(Build.VERSION_CODES.O)
object DateValidatorNoPast: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val todayStartMillis = LocalDate.now()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        return utcTimeMillis >= todayStartMillis
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}
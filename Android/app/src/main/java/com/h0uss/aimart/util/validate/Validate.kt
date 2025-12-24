package com.h0uss.aimart.util.validate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

fun String.validateName(): String{
    return if (this.length > 1) return "" else "Слишком короткое имя"
}

fun String.validateMail(): String{
    val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    return if (emailRegex.matches(this)) "" else "Некорректный email"
}

fun String.validatePassword(): String{
    return if (this.length >= 8) return "" else "Пароль должен содержать больше 8 символов"
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.validateDate(): String{
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT)
        formatter.parse(this)
        ""
    } catch (e: DateTimeParseException) {
        "Неверная дата рождения"
    }
}
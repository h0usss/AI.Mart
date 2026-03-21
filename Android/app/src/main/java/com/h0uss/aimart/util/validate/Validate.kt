package com.h0uss.aimart.util.validate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

fun String.validateName(): String{
    return if (this.length > 1) return "" else "Слишком короткое имя"
}

fun String.validateProductName(): String{
    return if (this.length > 5) return "" else "Слишком короткое название"
}

fun String.validateProductDesc(): String{
    return if (this.length > 10) return "" else "Слишком короткое описание"
}

fun String.validateProductPrice(): String{
    try{
        this.toFloat()
        return ""
    }
    catch (_: NumberFormatException){
        return "Введите корректно цену"
    }
}

fun List<String>.validateProductImages(): String{
    return if (this.isNotEmpty()) return "" else "Добавьте хотя бы одно изображение"
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
    } catch (_: DateTimeParseException) {
        "Неверная дата рождения"
    }
}
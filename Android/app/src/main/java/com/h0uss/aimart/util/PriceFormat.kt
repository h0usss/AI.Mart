package com.h0uss.aimart.util

import java.util.Locale

fun Float.formatPrice(): String {
    return if (this % 1.0f == 0f) {
        this.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", this).trimEnd('0').trimEnd('.')
    }
}

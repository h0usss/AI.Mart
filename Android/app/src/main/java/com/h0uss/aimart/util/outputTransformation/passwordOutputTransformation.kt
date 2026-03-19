package com.h0uss.aimart.util.outputTransformation

import androidx.compose.foundation.text.input.OutputTransformation

fun passwordOutputTransformation(): OutputTransformation {
    return OutputTransformation {
        this.replace(0, length, "•".repeat(length))
    }
}
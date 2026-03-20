package com.h0uss.aimart.util.inputTransformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete


fun floatInputTransformation(): InputTransformation = InputTransformation {
    val currentText = asCharSequence()

    val badChars = Regex("[^0-9.]")
    if (badChars.containsMatchIn(currentText)) {
        val filtered = currentText.toString().replace(badChars, "")
        replace(0, length, filtered)
    }

    val textAfterFilter = asCharSequence()
    val firstDotIndex = textAfterFilter.indexOf('.')

    if (firstDotIndex != -1) {
        val textAfterFirstDot = textAfterFilter.subSequence(firstDotIndex + 1, length)
        val secondDotIndex = textAfterFirstDot.indexOf('.')

        if (secondDotIndex != -1) {
            val absoluteSecondDotIndex = firstDotIndex + 1 + secondDotIndex
            delete(absoluteSecondDotIndex, absoluteSecondDotIndex + 1)
        }
    }
}
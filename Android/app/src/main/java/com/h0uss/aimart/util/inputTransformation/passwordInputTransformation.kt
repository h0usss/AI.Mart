package com.h0uss.aimart.util.inputTransformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert

fun passwordInputTransformation(): InputTransformation {
    val badList = listOf(':', ';', ' ', '\"', '\'')
    return InputTransformation {
        var ans = this.toString()
        ans = ans.filter { it !in badList }
        delete(0, length)
        insert(0, ans)
    }
}


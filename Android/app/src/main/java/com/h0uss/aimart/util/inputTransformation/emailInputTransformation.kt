package com.h0uss.aimart.util.inputTransformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert

fun emailInputTransformation(): InputTransformation {
    val badList = Regex("[^A-Za-z0-9@._-]")
    return InputTransformation {
        var ans = this.toString()
        ans = ans.replace(badList, "")
        delete(0, length)
        insert(0, ans)
    }
}


package com.h0uss.aimart.util.inputTransformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert

fun dateInputTransformation(): InputTransformation {
    val badList = Regex("[^0-9.]")
    return InputTransformation {
        var ans = this.toString()
        ans = ans.replace(badList, "")
        delete(0, length)
        insert(0, ans)
    }
}

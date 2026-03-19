package com.h0uss.aimart.util.inputTransformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert

fun nameInputTransformation(): InputTransformation {
    val badList = Regex("[^A-Za-zА-Яа-я]")
    return InputTransformation {
        var ans = this.toString()
        ans = ans.replace(badList, "")
        delete(0, length)
        insert(0, ans)
    }
}


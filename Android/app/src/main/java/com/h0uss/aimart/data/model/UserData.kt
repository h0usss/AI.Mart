package com.h0uss.aimart.data.model

import com.h0uss.aimart.R

data class UserData(
    var id: Long = -1L,
    var name: String = "",
    var nick: String = "",
    var imageUrl: String = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
    var rate: Float = -1f,
    var balance: Float = 0f,
    var isSeller: Boolean = false,
)

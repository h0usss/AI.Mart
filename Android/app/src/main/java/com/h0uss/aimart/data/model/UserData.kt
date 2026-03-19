package com.h0uss.aimart.data.model

import com.h0uss.aimart.R

data class UserData(
    var id: Long = -1L,
    var name: String = "",
    var nick: String = "",
    var imageId: Int = R.drawable.base_avatar,
    var rate: Float = -1f,
    var balance: String = "0.00",
)

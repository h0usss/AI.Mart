package com.h0uss.aimart.data.model

import com.h0uss.aimart.R

data class SellerData(
    var id: Long = -1L,
    var name: String = "",
    var balance: String = "0.00",
    var nick: String = "",
    var imageId: Int = R.drawable.base_avatar,
    var rate: Float = 0f,
    var profession: String = "",
    var about: String = "",
    var skills: List<String> = listOf(),
)

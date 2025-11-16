package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.OrderStatus

data class ProductCardData(
    val author: String,
    val name: String,
    val price: Float,
    val imageId: Int,
    val status: OrderStatus?
)

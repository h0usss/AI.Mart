package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.OrderStatus

data class OrderCardData(
    val id: Long,
    val name: String,
    val price: Float,
    val status: OrderStatus,
    val imageId: Int,
)
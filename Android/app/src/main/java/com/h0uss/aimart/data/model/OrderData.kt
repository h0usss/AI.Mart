package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.OrderStatus
import java.time.LocalDateTime

class OrderData(
    val id: Long = -1L,
    val price: Float = -1f,
    val status: OrderStatus = OrderStatus.DELETED,
    val description: String? = null,
    val deadline: LocalDateTime? = null,
    val startDate: LocalDateTime? = null,
    val completionDate: LocalDateTime? = null,
    val buyerId: Long = -1L,
    val sellerId: Long = -1L,
    val productId: Long = -1L,
)
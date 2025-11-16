package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.OrderStatus

data class StatusData(
    val status: OrderStatus,
    val count: Int = 0,
    val isTag: Boolean = false,
)

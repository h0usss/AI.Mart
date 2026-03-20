package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.ProductStatus

data class UserProductCardData(
    val id: Long,
    val name: String,
    val price: Float,
    val imagesId: List<Int>,
    val description: String,
    val status: ProductStatus,
)

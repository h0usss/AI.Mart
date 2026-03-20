package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.ProductStatus

data class ProductData(
    val productId: Long = -1L,
    val userId: Long = -1L,
    val author: String = "",
    val name: String = "",
    val price: Float = 0f,
    val imagesId: List<Int> = listOf(),
    val status: ProductStatus = ProductStatus.ACTIVE
)

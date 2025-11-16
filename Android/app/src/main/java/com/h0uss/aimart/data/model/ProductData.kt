package com.h0uss.aimart.data.model

import com.h0uss.aimart.data.emun.ProductStatus

data class ProductData(
    val author: String,
    val price: Float,
    val imageId: Int,
    val status: ProductStatus
)

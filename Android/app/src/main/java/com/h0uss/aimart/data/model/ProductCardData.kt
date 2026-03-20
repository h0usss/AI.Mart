package com.h0uss.aimart.data.model

data class ProductCardData(
    val id: Long,
    val authorName: String,
    val name: String,
    val price: Float,
    val imagesId: List<Int>,
    val description: String
)

package com.h0uss.aimart.data.model

data class PortfolioItemData(
    var id: Long,
    var imageId: Int,
    var name: String,
    var tags: List<String>
)

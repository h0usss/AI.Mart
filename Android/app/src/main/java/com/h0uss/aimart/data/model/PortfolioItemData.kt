package com.h0uss.aimart.data.model

data class PortfolioItemData(
    var id: Long,
    var media: List<Int>,
    var title: String,
    var description: String,
    var tags: List<String>
)

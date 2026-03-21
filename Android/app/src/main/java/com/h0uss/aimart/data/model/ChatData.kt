package com.h0uss.aimart.data.model

class ChatData (
    val id: Long = -1L,
    val imagesUrl: List<String> = listOf(),
    val productName: String? = null,
    val userName: String = "",
    val price: Float? = null,
)
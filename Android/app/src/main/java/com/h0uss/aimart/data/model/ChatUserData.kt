package com.h0uss.aimart.data.model

class ChatUserData (
    val userId: Long = -1L,
    val productId: Long? = null,
    val imagesUrl: List<String> = listOf(),
    val userName: String = "",
)
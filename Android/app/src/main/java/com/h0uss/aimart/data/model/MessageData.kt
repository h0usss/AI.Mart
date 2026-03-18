package com.h0uss.aimart.data.model

import java.time.LocalDateTime

class MessageData (
    val text: String,
    val date: LocalDateTime,
    val avatarId: Int = -1,
    val userId: Long = -1L,
)
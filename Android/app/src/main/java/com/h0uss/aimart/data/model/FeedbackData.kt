package com.h0uss.aimart.data.model

import java.time.LocalDateTime

data class FeedbackData(
    var user: UserData,
    var text: String,
    var starCount: Int,
    var date: LocalDateTime
)
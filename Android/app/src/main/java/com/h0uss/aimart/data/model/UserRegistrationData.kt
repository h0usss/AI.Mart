package com.h0uss.aimart.data.model

import java.time.LocalDateTime

data class UserRegistrationData(
    val name: String,
    val email: String,
    val password: String,
    val dateOfBirth: LocalDateTime,
)

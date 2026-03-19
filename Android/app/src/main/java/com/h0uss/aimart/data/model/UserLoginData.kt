package com.h0uss.aimart.data.model

data class UserLoginData(
    val id: Long,
    val email: String,
    val nick: String,
    val passwordHash: String,
)

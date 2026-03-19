package com.h0uss.aimart.data.model

data class AlertData(
    val title: String,
    val description: String = "",
    val leftText: String,
    val rightText: String,
    val leftClick: () -> Unit = {},
    val rightClick: () -> Unit = {},
)
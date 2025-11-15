package com.h0uss.aimart.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import com.h0uss.aimart.R

val Inter = FontFamily(
    Font( resId = R.font.inter_regular, weight = FontWeight.Normal ),
    Font( resId = R.font.inter_semibold, weight = FontWeight.SemiBold )
)

val regularStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal
)
val semiboldStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold
)
package com.h0uss.aimart.data.model

import androidx.compose.ui.graphics.painter.Painter
import com.h0uss.aimart.ui.ScreensRoutes

class BottomNavBarItem(
    val label: String,
    val route: ScreensRoutes,
    val image: Painter,
)
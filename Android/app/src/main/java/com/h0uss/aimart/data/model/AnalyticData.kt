package com.h0uss.aimart.data.model

enum class AnalyticPeriod {
    YEAR, MONTH, WEEK,
}

data class AnalyticBar(
    val label: String,
    val value: Float,
    val subtitle: String,
    val viewCount: Int = 0,
)

data class AnalyticData(
    val bars: List<AnalyticBar> = emptyList(),
    val selectedIndex: Int = 0,
    val totalViews: Int = 0,
    val isViewsMode: Boolean = false,
)

package com.h0uss.aimart.ui.assets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.data.model.AnalyticBar
import com.h0uss.aimart.data.model.AnalyticData
import com.h0uss.aimart.data.model.AnalyticPeriod
import com.h0uss.aimart.ui.theme.AnalyticGray
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black45
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.TealCounterText
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.util.formatPrice

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Analytic(
    modifier: Modifier = Modifier,
    data: AnalyticData,
    selectedPeriod: AnalyticPeriod,
    onPeriodChange: (AnalyticPeriod) -> Unit,
    onBarSelect: (Int) -> Unit,
    onViewsModeToggle: () -> Unit = {},
) {
    val periods = listOf(
        AnalyticPeriod.YEAR to "За год",
        AnalyticPeriod.MONTH to "За месяц",
        AnalyticPeriod.WEEK to "За неделю",
    )
    val selectedBar = data.bars.getOrNull(data.selectedIndex)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(15.dp),
            )
            .background(White)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Выручка",
                style = if (!data.isViewsMode) semiboldStyle else regularStyle,
                fontSize = 18.sp,
                color = if (!data.isViewsMode) TealCounterText else Black45,
                modifier = Modifier.clickable { if (data.isViewsMode) onViewsModeToggle() },
            )
            Text(
                text = "Просмотры",
                style = if (data.isViewsMode) semiboldStyle else regularStyle,
                fontSize = 18.sp,
                color = if (data.isViewsMode) TealCounterText else Black45,
                modifier = Modifier.clickable { if (!data.isViewsMode) onViewsModeToggle() },
            )
        }

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            periods.forEach { (period, label) ->
                val isSelected = period == selectedPeriod
                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Black5 else White)
                        .clickable { onPeriodChange(period) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    style = if (isSelected) semiboldStyle else regularStyle,
                    fontSize = 14.sp,
                    color = if (isSelected) Black100 else Black45,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedBar?.subtitle.orEmpty(),
                style = regularStyle,
                fontSize = 14.sp,
                color = Black80,
            )
            if (data.isViewsMode) {
                Text(
                    text = selectedBar?.viewCount?.toString().orEmpty(),
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = TealCounterText,
                )
            } else {
                Text(
                    text = "${selectedBar?.value?.formatPrice().orEmpty()}₽",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = TealCounterText,
                )
            }
        }

        AnalyticBarChart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(120.dp),
            bars = data.bars,
            selectedIndex = data.selectedIndex,
            onBarSelect = onBarSelect,
            isViewsMode = data.isViewsMode,
        )
    }
}

@Composable
private fun AnalyticBarChart(
    modifier: Modifier,
    bars: List<AnalyticBar>,
    selectedIndex: Int,
    onBarSelect: (Int) -> Unit,
    isViewsMode: Boolean = false,
) {
    if (bars.isEmpty()) return

    val maxValue = if (isViewsMode) {
        bars.maxOf { it.viewCount.toFloat() }.coerceAtLeast(1f)
    } else {
        bars.maxOf { it.value }.coerceAtLeast(1f)
    }
    val chartHeight = 90.dp

    if (bars.size <= 7) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            bars.forEachIndexed { index, bar ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onBarSelect(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    val rawHeight =
                        (if (isViewsMode) bar.viewCount.toFloat() else bar.value) / maxValue * chartHeight.value
                    val targetDp = rawHeight.dp.coerceAtLeast(4.dp)
                    val animatedHeight by animateDpAsState(
                        targetValue = targetDp,
                        animationSpec = tween(durationMillis = 400),
                    )
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(animatedHeight)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == selectedIndex) Teal else AnalyticGray),
                    )
                    Text(
                        text = bar.label,
                        modifier = Modifier.padding(top = 6.dp),
                        style = regularStyle,
                        fontSize = 11.sp,
                        color = Black45,
                    )
                }
            }
        }
    } else {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            itemsIndexed(bars) { index, bar ->
                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .clickable { onBarSelect(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    val rawHeight =
                        (if (isViewsMode) bar.viewCount.toFloat() else bar.value) / maxValue * chartHeight.value
                    val targetDp = rawHeight.dp.coerceAtLeast(4.dp)
                    val animatedHeight by animateDpAsState(
                        targetValue = targetDp,
                        animationSpec = tween(durationMillis = 400),
                    )
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(animatedHeight)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == selectedIndex) Teal else AnalyticGray),
                    )
                    Text(
                        text = bar.label,
                        modifier = Modifier.padding(top = 6.dp),
                        style = regularStyle,
                        fontSize = 11.sp,
                        color = Black45,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PreviewWeek() {
    Analytic(
        data = AnalyticData(
            bars = listOf(
                AnalyticBar("пн", 10f, "2 июня, пн", viewCount = 5),
                AnalyticBar("вт", 20f, "3 июня, вт", viewCount = 12),
                AnalyticBar("ср", 5f, "4 июня, ср", viewCount = 3),
                AnalyticBar("чт", 15f, "5 июня, чт", viewCount = 8),
                AnalyticBar("пт", 8f, "6 июня, пт", viewCount = 6),
                AnalyticBar("сб", 12f, "7 июня, сб", viewCount = 9),
                AnalyticBar("вс", 3f, "8 июня, вс", viewCount = 2),
            ),
            selectedIndex = 1,
            totalViews = 45,
        ),
        selectedPeriod = AnalyticPeriod.WEEK,
        onPeriodChange = {},
        onBarSelect = {},
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PreviewYear() {
    Analytic(
        data = AnalyticData(
            bars = listOf(
                AnalyticBar("20", 1200f, "2020 год", viewCount = 50),
                AnalyticBar("21", 800f, "2021 год", viewCount = 30),
                AnalyticBar("22", 1500f, "2022 год", viewCount = 70),
                AnalyticBar("23", 900f, "2023 год", viewCount = 40),
                AnalyticBar("24", 2100f, "2024 год", viewCount = 90),
                AnalyticBar("25", 1800f, "2025 год", viewCount = 60),
                AnalyticBar("26", 3500f, "2026 год", viewCount = 120),
            ),
            selectedIndex = 6,
            totalViews = 460,
        ),
        selectedPeriod = AnalyticPeriod.YEAR,
        onPeriodChange = {},
        onBarSelect = {},
    )
}

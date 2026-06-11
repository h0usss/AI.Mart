package com.h0uss.aimart.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.dao.ProductViewDao
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.AnalyticBar
import com.h0uss.aimart.data.model.AnalyticData
import com.h0uss.aimart.data.model.AnalyticPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class AnalyticsRepository(
    private val orderDao: OrderDao,
    private val productViewDao: ProductViewDao,
) {

    fun getSellerAnalytics(
        sellerId: Long,
        period: AnalyticPeriod,
        selectedBarIndex: Int,
    ): Flow<AnalyticData> = orderDao.getCompletedOrdersBySellerId(sellerId).map { orders ->
        val now = LocalDate.now()
        val barsData = when (period) {
            AnalyticPeriod.WEEK -> buildWeekBars(orders, now)
            AnalyticPeriod.MONTH -> buildMonthBars(orders, now)
            AnalyticPeriod.YEAR -> buildYearBars(orders, now)
        }
        val (bars, defaultIndex) = barsData
        if (bars.isEmpty()) return@map AnalyticData()

        val index = if (selectedBarIndex >= 0) {
            selectedBarIndex.coerceIn(0, bars.lastIndex)
        } else {
            defaultIndex
        }

        val yearsForYearPeriod: List<Int> = if (period == AnalyticPeriod.YEAR) {
            orders.mapNotNull { it.completionDate?.toLocalDate()?.year }.distinct().sortedDescending().take(7)
        } else emptyList()

        val (startDate, endDate) = barDateRange(period, index, now, yearsForYearPeriod)
        val viewCount = productViewDao.getUniqueViewsBySellerIdBetween(sellerId, startDate, endDate)

        AnalyticData(
            bars = bars,
            selectedIndex = index,
            totalViews = viewCount.toInt(),
        )
    }

    private fun barDateRange(period: AnalyticPeriod, index: Int, now: LocalDate, years: List<Int>): Pair<LocalDateTime, LocalDateTime> {
        return when (period) {
            AnalyticPeriod.WEEK -> {
                val weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val day = weekStart.plusDays(index.toLong())
                day.atStartOfDay() to day.plusDays(1).atStartOfDay()
            }
            AnalyticPeriod.MONTH -> {
                val start = LocalDate.of(now.year, index + 1, 1)
                val end = start.plusMonths(1)
                start.atStartOfDay() to end.atStartOfDay()
            }
            AnalyticPeriod.YEAR -> {
                val year = years.getOrElse(index) { now.year }
                val start = LocalDate.of(year, 1, 1)
                start.atStartOfDay() to start.plusYears(1).atStartOfDay()
            }
        }
    }

    private fun buildWeekBars(
        orders: List<OrderEntity>,
        now: LocalDate,
    ): Pair<List<AnalyticBar>, Int> {
        val weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val dayLabelFormatter = DateTimeFormatter.ofPattern("EE", Locale("ru"))
        val subtitleFormatter = DateTimeFormatter.ofPattern("d MMMM, EE", Locale("ru"))

        val days = (0..6).map { weekStart.plusDays(it.toLong()) }
        val bars = days.map { day ->
            val revenue = orders
                .filter { it.completionDate?.toLocalDate() == day }
                .sumOf { it.price.toDouble() }
                .toFloat()
            AnalyticBar(
                label = day.format(dayLabelFormatter).removeSuffix(".").take(2),
                value = revenue,
                subtitle = day.format(subtitleFormatter),
            )
        }
        val selectedIndex = days.indexOf(now).coerceIn(0, 6)
        return bars to selectedIndex
    }

    private fun buildMonthBars(
        orders: List<OrderEntity>,
        now: LocalDate,
    ): Pair<List<AnalyticBar>, Int> {
        val monthLabelFormatter = DateTimeFormatter.ofPattern("LLL", Locale("ru"))
        val months = (1..12).map { YearMonth.of(now.year, it) }

        val bars = months.map { ym ->
            val revenue = orders
                .filter {
                    val date = it.completionDate?.toLocalDate()
                    date?.year == ym.year && date.monthValue == ym.monthValue
                }
                .sumOf { it.price.toDouble() }
                .toFloat()
            val monthDate = LocalDate.of(ym.year, ym.month, 1)
            AnalyticBar(
                label = monthDate.format(monthLabelFormatter).removeSuffix("."),
                value = revenue,
                subtitle = monthDate.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))),
            )
        }
        val selectedIndex = now.monthValue - 1
        return bars to selectedIndex
    }

    private fun buildYearBars(
        orders: List<OrderEntity>,
        now: LocalDate,
    ): Pair<List<AnalyticBar>, Int> {
        val years = orders
            .mapNotNull { it.completionDate?.toLocalDate()?.year }
            .distinct()
            .sortedDescending()
            .take(7)

        if (years.isEmpty()) return emptyList<AnalyticBar>() to 0

        val bars = years.map { year ->
            val revenue = orders
                .filter { it.completionDate?.toLocalDate()?.year == year }
                .sumOf { it.price.toDouble() }
                .toFloat()
            AnalyticBar(
                label = year.toString().takeLast(2),
                value = revenue,
                subtitle = "$year год",
            )
        }
        val selectedIndex = bars.lastIndex
        return bars to selectedIndex
    }
}

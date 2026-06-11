package com.h0uss.aimart.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.dao.ProductDao
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.AnalyticBar
import com.h0uss.aimart.data.model.AnalyticData
import com.h0uss.aimart.data.model.AnalyticPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class AnalyticsRepository(
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
) {

    fun getSellerAnalytics(
        sellerId: Long,
        period: AnalyticPeriod,
        selectedBarIndex: Int,
    ): Flow<AnalyticData> = combine(
        orderDao.getCompletedOrdersBySellerId(sellerId),
        productDao.getTotalViewCountBySellerId(sellerId),
    ) { orders, totalViews ->
        buildAnalyticData(
            orders = orders,
            period = period,
            selectedBarIndex = selectedBarIndex,
            totalViews = totalViews.toInt(),
        )
    }

    private fun buildAnalyticData(
        orders: List<OrderEntity>,
        period: AnalyticPeriod,
        selectedBarIndex: Int,
        totalViews: Int,
    ): AnalyticData {
        val now = LocalDate.now()
        val (bars, defaultIndex) = when (period) {
            AnalyticPeriod.WEEK -> buildWeekBars(orders, now)
            AnalyticPeriod.MONTH -> buildMonthBars(orders, now)
            AnalyticPeriod.YEAR -> buildYearBars(orders, now)
        }
        val index = if (selectedBarIndex >= 0 && bars.isNotEmpty()) {
            selectedBarIndex.coerceIn(0, bars.lastIndex)
        } else {
            defaultIndex
        }

        return AnalyticData(
            bars = bars,
            selectedIndex = index,
            totalViews = totalViews,
        )
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
        val months = (6 downTo 0).map { now.minusMonths(it.toLong()) }

        val bars = months.map { month ->
            val revenue = orders
                .filter {
                    val date = it.completionDate?.toLocalDate()
                    date?.year == month.year && date.monthValue == month.monthValue
                }
                .sumOf { it.price.toDouble() }
                .toFloat()
            AnalyticBar(
                label = month.format(monthLabelFormatter).removeSuffix("."),
                value = revenue,
                subtitle = month.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))),
            )
        }
        val selectedIndex = bars.lastIndex
        return bars to selectedIndex
    }

    private fun buildYearBars(
        orders: List<OrderEntity>,
        now: LocalDate,
    ): Pair<List<AnalyticBar>, Int> {
        val years = (6 downTo 0).map { now.year - it }

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
        val selectedIndex = years.indexOf(now.year).coerceIn(0, bars.lastIndex)
        return bars to selectedIndex
    }
}

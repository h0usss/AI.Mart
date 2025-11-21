package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.model.OrderCardData
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val orderDao: OrderDao
) {

    fun getOrderByUserId(userId: Long): Flow<List<OrderCardData>>{
        return orderDao.getOrderBySellerId(userId)
    }
}
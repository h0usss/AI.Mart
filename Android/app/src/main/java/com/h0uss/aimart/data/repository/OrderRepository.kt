package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.OrderCardData
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val orderDao: OrderDao
) {

    suspend fun insert(order: OrderEntity): Long {
        return orderDao.insert(order)
    }

    fun getOrderByUserId(userId: Long): Flow<List<OrderCardData>>{
        return orderDao.getOrderBySellerId(userId)
    }
}
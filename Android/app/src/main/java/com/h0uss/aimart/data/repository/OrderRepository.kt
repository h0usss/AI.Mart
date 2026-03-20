package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.OrderCardData
import com.h0uss.aimart.data.model.OrderData
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val orderDao: OrderDao
) {

    suspend fun insert(order: OrderEntity): Long {
        return orderDao.insert(order)
    }

    fun getOrdersByUserId(userId: Long): Flow<List<OrderCardData>>{
        return orderDao.getOrdersBySellerId(userId)
    }

    fun getOrderById(orderId: Long): Flow<OrderData>{
        return orderDao.getOrderById(orderId)
    }

    suspend fun updateStatus(orderId: Long, status: OrderStatus){
        orderDao.updateOrderStatus(orderId, status)
    }
}
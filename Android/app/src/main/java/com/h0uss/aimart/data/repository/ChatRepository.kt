package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.ChatDao

class ChatRepository(
    private val chatDao: ChatDao
) {

//    fun getOrderByUserId(userId: Long): Flow<List<OrderCardData>>{
//        return orderDao.getOrderBySellerId(userId)
//    }
}
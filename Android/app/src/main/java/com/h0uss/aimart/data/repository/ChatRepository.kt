package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.ChatDao
import com.h0uss.aimart.data.entity.ChatEntity
import com.h0uss.aimart.data.model.ChatData
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao
) {

    suspend fun insert(chat: ChatEntity): Long {
        return chatDao.insert(chat)
    }

    fun getAllUserChats(userId: Long): Flow<List<ChatData>> {
        return chatDao.getAllUserChats(userId)
    }

    fun getChatNoProduct(myId: Long, sUserId: Long): Flow<ChatData?> {
        return chatDao.getChatNoProduct(myId, sUserId)
    }

    fun getChatByProductId(productId: Long, myId: Long): Flow<List<ChatData>> {
        return chatDao.getChatByProductId(productId, myId)
    }
}
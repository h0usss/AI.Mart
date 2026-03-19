package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.ChatDao
import com.h0uss.aimart.data.model.ChatData
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao
) {

    fun getAllUserChats(userId: Long): Flow<List<ChatData>> {
        return chatDao.getAllUserChats(userId)
    }
}
package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.MessageDao
import com.h0uss.aimart.data.model.MessageData
import kotlinx.coroutines.flow.Flow

class MessageRepository(
    private val messageDao: MessageDao
) {

    fun getMessagesByChatId(chatId: Long): Flow<List<MessageData>> {
        return messageDao.getMessagesByChatId(chatId)
    }
}
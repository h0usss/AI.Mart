package com.h0uss.aimart.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.dao.MessageDao
import com.h0uss.aimart.data.entity.MessageEntity
import com.h0uss.aimart.data.model.MessageData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MessageRepository(
    private val messageDao: MessageDao
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addMessageToChat(chatId: Long, senderId: Long, text: String) {
        val newMessage = MessageEntity(
            chatId = chatId,
            senderId = senderId,
            message = text,
            createdAt = LocalDateTime.now()
        )
        messageDao.insertMessage(newMessage)
    }

    fun getMessagesByChatId(chatId: Long): Flow<List<MessageData>> {
        return messageDao.getMessagesByChatId(chatId)
    }
}
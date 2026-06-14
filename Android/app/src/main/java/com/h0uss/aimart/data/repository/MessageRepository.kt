package com.h0uss.aimart.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.dao.MessageDao
import com.h0uss.aimart.data.entity.MessageEntity
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.util.MediaResizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MessageRepository(
    private val messageDao: MessageDao,
    private val context: Context,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addMessageToChat(
        chatId: Long,
        senderId: Long,
        text: String,
        isProtected: Boolean = false
    ) {
        val newMessage = MessageEntity(
            chatId = chatId,
            senderId = senderId,
            message = text,
            createdAt = LocalDateTime.now(),
            isProtected = isProtected,
        )
        messageDao.insertMessage(newMessage)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun addMessageToChat(
        chatId: Long,
        senderId: Long,
        text: String,
        attachments: List<String>,
        isProtected: Boolean = false
    ) {
        val processed = if (isProtected) {
            withContext(Dispatchers.IO) {
                attachments.map { MediaResizer.resizeAttachment(context, it) }
            }
        } else attachments
        val newMessage = MessageEntity(
            chatId = chatId,
            senderId = senderId,
            message = text,
            attachments = processed,
            createdAt = LocalDateTime.now(),
            isProtected = isProtected,
        )
        messageDao.insertMessage(newMessage)
    }

    fun getMessagesByChatId(chatId: Long): Flow<List<MessageData>> {
        return messageDao.getMessagesByChatId(chatId)
    }
}
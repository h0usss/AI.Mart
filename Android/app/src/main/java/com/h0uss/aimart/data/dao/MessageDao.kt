package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h0uss.aimart.data.entity.MessageEntity
import com.h0uss.aimart.data.model.MessageData
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(messages: List<MessageEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query(
        """
        SELECT
            m.message AS text,
            m.created_at AS date,
            u.avatar AS avatarId,
            u.id AS userId
        FROM messages AS m
        JOIN user AS u ON m.sender_id = u.id
        WHERE m.chat_id = :chatId
        ORDER BY created_at DESC
    """
    )
    fun getMessagesByChatId(chatId: Long): Flow<List<MessageData>>
}
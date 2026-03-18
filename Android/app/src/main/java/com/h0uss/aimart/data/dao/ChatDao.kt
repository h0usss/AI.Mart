package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h0uss.aimart.data.entity.ChatEntity
import com.h0uss.aimart.data.model.ChatData
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(chats: List<ChatEntity>): List<Long>


    @Query("""
        SELECT * 
        FROM chats
        ORDER BY created_at DESC
    """)
    fun getAll(): Flow<List<ChatEntity>>

    @Query(
        """
        SELECT
            c.id AS id,
            p.image AS imageId,
            p.name AS productName,
            u.name AS userName,
            p.price AS price
        FROM chats AS c
        JOIN product AS p ON c.product_id = p.id
        JOIN user AS u ON p.user_id = u.id
        WHERE f_user_id = :userId OR s_user_id = :userId
        ORDER BY created_at DESC
    """
    )
    fun getAllUserChats(userId: Long): Flow<List<ChatData>>
}
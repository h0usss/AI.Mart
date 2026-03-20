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
            p.images AS imagesId,
            p.name AS productName,
            CASE 
                WHEN c.f_user_id = :userId THEN u2.name 
                ELSE u1.name 
            END AS userName,
            p.price AS price
        FROM chats AS c
        JOIN product AS p ON c.product_id = p.id
        JOIN user AS u1 ON c.f_user_id = u1.id
        JOIN user AS u2 ON c.s_user_id = u2.id
        WHERE c.f_user_id = :userId OR c.s_user_id = :userId
        ORDER BY c.created_at DESC
    """
    )
    fun getAllUserChats(userId: Long): Flow<List<ChatData>>
}
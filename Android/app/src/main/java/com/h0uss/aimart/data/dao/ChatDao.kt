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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chats: ChatEntity): Long


    @Query("""
        SELECT * 
        FROM chats
        ORDER BY created_at DESC
    """)
    fun getAll(): Flow<List<ChatEntity>>

    @Query("""
        SELECT * 
        FROM chats
        WHERE id = :chatId
    """)
    fun getChatById(chatId: Long): Flow<ChatEntity?>

    @Query("""
        
        SELECT
            c.id AS id,
            COALESCE(p.images, 
                CASE WHEN c.f_user_id = :userId THEN u2.avatar ELSE u1.avatar END
            ) AS imagesUrl,
            p.name AS productName,
            CASE 
                WHEN c.f_user_id = :userId THEN u2.name 
                ELSE u1.name 
            END AS userName,
            o.price AS price
        FROM chats AS c
        LEFT JOIN orders AS o ON c.order_id = o.id
        LEFT JOIN product AS p ON o.product_id = p.id    
        JOIN user AS u1 ON c.f_user_id = u1.id
        JOIN user AS u2 ON c.s_user_id = u2.id
        WHERE c.f_user_id = :userId OR c.s_user_id = :userId
        ORDER BY c.created_at DESC
    """)
    fun getAllUserChats(userId: Long): Flow<List<ChatData>>

    @Query("""
        SELECT
            c.id AS id,
            p.images AS imagesUrl,
            p.name AS productName,
            CASE 
                WHEN c.f_user_id = :myId THEN u2.name 
                ELSE u1.name 
            END AS userName,
            p.price AS price
        FROM chats AS c
        LEFT JOIN orders AS o ON c.order_id = o.id
        JOIN user AS u1 ON c.f_user_id = u1.id
        JOIN user AS u2 ON c.s_user_id = u2.id
        JOIN product AS p ON o.product_id = p.id
        WHERE o.id = :orderId
        ORDER BY c.created_at DESC
    """)
    fun getChatByOrderId(orderId: Long, myId: Long): Flow<List<ChatData>>

    @Query("""
        SELECT 
            c.id,
            CASE WHEN c.f_user_id = :myId THEN u2.avatar ELSE u1.avatar END AS imagesUrl,
            CASE WHEN c.f_user_id = :myId THEN u2.name ELSE u1.name END AS userName,
            '' AS productName,
            0.0 AS price
        FROM chats c
        JOIN user u1 ON c.f_user_id = u1.id
        JOIN user u2 ON c.s_user_id = u2.id
        WHERE ((c.f_user_id = :myId AND c.s_user_id = :sUserId) 
           OR (c.f_user_id = :sUserId AND c.s_user_id = :myId))
           AND c.order_id IS NULL
        LIMIT 1
    """)
    fun getChatNoOrder(myId: Long, sUserId: Long): Flow<ChatData?>
}

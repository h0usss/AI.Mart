package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.OrderCardData
import com.h0uss.aimart.data.model.OrderData
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(orders: List<OrderEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(order: OrderEntity): Long

    @Query("""
        SELECT
        o.id AS id,
        p.name AS name,
        o.price AS price,
        o.status AS status,
        p.images AS imagesUrl
        FROM orders AS o
        JOIN product AS p ON o.product_id = p.id
        WHERE o.seller_id = :userId AND o.status != 'DELETED'
    """)
    fun getOrdersBySellerId(userId: Long): Flow<List<OrderCardData>>

    @Query("""
        SELECT 
            o.id AS id,
            o.price AS price,
            o.status AS status,
            o.description AS description,
            o.deadline AS deadline,
            o.start_date AS startDate,
            o.completion_date AS completionDate,
            o.buyer_id AS buyerId,
            o.seller_id AS sellerId,
            o.product_id AS productId
        FROM orders AS o
        WHERE o.id = :orderId
    """)
    fun getOrderById(orderId: Long): Flow<OrderData>

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)

}
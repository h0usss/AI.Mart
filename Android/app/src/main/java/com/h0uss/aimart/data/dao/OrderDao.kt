package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.model.OrderCardData
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<OrderEntity>): List<Long>
    @Query("""
        SELECT
        o.id AS id,
        p.name AS name,
        o.price AS price,
        o.status AS status,
        p.image AS imageId
        FROM orders AS o
        JOIN product AS p ON o.product_id = p.id
        WHERE o.seller_id = :userId
    """)
    fun getOrderBySellerId(userId: Long): Flow<List<OrderCardData>>
}
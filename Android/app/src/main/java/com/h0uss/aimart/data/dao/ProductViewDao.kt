package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.h0uss.aimart.data.entity.ProductViewEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ProductViewDao {

    @Insert
    suspend fun insert(view: ProductViewEntity)

    @Query("SELECT COUNT(*) FROM (SELECT 1 FROM product_view WHERE viewed_at >= :startDate AND viewed_at < :endDate AND product_id IN (SELECT id FROM product WHERE user_id = :sellerId) GROUP BY product_id, user_id)")
    suspend fun getUniqueViewsBySellerIdBetween(
        sellerId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Long

    @Query("SELECT COUNT(*) FROM (SELECT 1 FROM product_view WHERE product_id = :productId AND viewed_at >= :startDate AND viewed_at < :endDate GROUP BY product_id, user_id)")
    fun getUniqueViewsByProductIdBetween(
        productId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<Long>

    @Query("SELECT COUNT(*) FROM (SELECT 1 FROM product_view WHERE product_id = :productId GROUP BY product_id, user_id)")
    fun getTotalUniqueViewsByProductId(productId: Long): Flow<Long>
}

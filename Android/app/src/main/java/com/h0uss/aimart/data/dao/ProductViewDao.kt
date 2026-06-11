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

    @Query("SELECT COUNT(DISTINCT user_id) FROM product_view WHERE viewed_at >= :startDate AND viewed_at < :endDate AND product_id IN (SELECT id FROM product WHERE user_id = :sellerId)")
    suspend fun getUniqueViewsBySellerIdBetween(sellerId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Long

    @Query("SELECT COUNT(DISTINCT user_id) FROM product_view WHERE product_id = :productId AND viewed_at >= :startDate AND viewed_at < :endDate")
    fun getUniqueViewsByProductIdBetween(productId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Flow<Long>

    @Query("SELECT COUNT(DISTINCT user_id) FROM product_view WHERE product_id = :productId")
    fun getTotalUniqueViewsByProductId(productId: Long): Flow<Long>
}

package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.h0uss.aimart.data.entity.PortfolioItemEntity
import com.h0uss.aimart.data.entity.PortfolioItemWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<PortfolioItemEntity>)

    @Transaction
    @Query("""
        SELECT * 
        FROM portfolio_item
        WHERE user_id = :sellerId
    """)
    fun getPortfolioBySellerIdFlow(sellerId: Long): Flow<List<PortfolioItemWithTags>>

    @Query("""
        DELETE FROM portfolio_item
        WHERE id = :portfolioItemId
    """)
    suspend fun deletePortfolioItem(portfolioItemId: Long)
}
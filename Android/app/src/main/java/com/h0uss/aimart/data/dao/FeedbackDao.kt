package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.h0uss.aimart.data.entity.FeedbackEntity
import com.h0uss.aimart.data.entity.FeedbackWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<FeedbackEntity>)
    @Transaction
    @Query("""
        SELECT * 
        FROM FeedbackWithUserReferenceView AS f
        WHERE seller_id = :sellerId
    """)
    fun getFeedbackBySellerIdFlow(sellerId: Long): Flow<List<FeedbackWithUser>>

}

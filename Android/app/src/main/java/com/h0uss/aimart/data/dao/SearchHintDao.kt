package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.h0uss.aimart.data.entity.SearchHintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHintDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hint: SearchHintEntity)

    @Upsert
    suspend fun upsert(hint: SearchHintEntity)

    @Query("""
        SELECT * 
        FROM search_hint 
        WHERE user_id = :userId 
        ORDER BY timestamp DESC
    """)
    fun getHints(userId: Long): Flow<List<SearchHintEntity>>

    @Query("""
        DELETE 
        FROM search_hint 
        WHERE user_id = :userId AND text = :hintText
    """)
    suspend fun deleteHint(userId: Long, hintText: String)

    @Query("""
        DELETE 
        FROM search_hint 
        WHERE user_id = :userId
    """)
    suspend fun deleteAllForUser(userId: Long)
}
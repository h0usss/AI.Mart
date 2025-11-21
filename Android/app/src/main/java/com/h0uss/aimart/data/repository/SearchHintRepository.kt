package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.SearchHintDao
import com.h0uss.aimart.data.entity.SearchHintEntity
import kotlinx.coroutines.flow.Flow

class SearchHintRepository(
    private val searchHintDao: SearchHintDao
) {
    fun getHints(userId: Long): Flow<List<SearchHintEntity>> {
        return searchHintDao.getHints(userId)
    }

    suspend fun updateTimeHint(userId: Long, hintText: String) {
        searchHintDao.upsert(
            hint = SearchHintEntity(
                userId = userId,
                text = hintText
            )
        )
    }

    suspend fun deleteHint(userId: Long, hintText: String) {
        searchHintDao.deleteHint(userId, hintText)
    }

    suspend fun deleteAllHint(userId: Long) {
        searchHintDao.deleteAllForUser(userId)
    }

    suspend fun addHintIfNotExist(userId: Long, hintText: String) {
        searchHintDao.insert(
            hint = SearchHintEntity(
                userId = userId,
                text = hintText
            )
        )
    }
}
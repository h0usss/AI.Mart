package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.FeedbackDao
import com.h0uss.aimart.data.mapper.toUserData
import com.h0uss.aimart.data.model.FeedbackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedbackRepository(
    private val feedbackDao: FeedbackDao
) {
    fun getFeedbackBySellerIdFlow(userId: Long) : Flow<List<FeedbackData>> {
        return feedbackDao.getFeedbackBySellerIdFlow(userId).map { list ->
            list.map{ item ->
                FeedbackData(
                    user = item.user.toUserData(),
                    text = item.feedbackView.feedback.text,
                    starCount = item.feedbackView.feedback.starCount,
                    date = item.feedbackView.feedback.timestamp
                )
            }
        }

    }
}
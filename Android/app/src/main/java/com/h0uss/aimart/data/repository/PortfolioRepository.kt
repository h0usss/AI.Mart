package com.h0uss.aimart.data.repository

import com.h0uss.aimart.data.dao.PortfolioDao
import com.h0uss.aimart.data.model.PortfolioItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PortfolioRepository(
    private val portfolioDao: PortfolioDao
) {
    fun getPortfolioBySellerIdFlow(userId: Long): Flow<List<PortfolioItemData>> {
        return portfolioDao.getPortfolioBySellerIdFlow(userId).map { list ->
            list.map{ item ->
                PortfolioItemData(
                    id = item.portfolioItem.id,
                    imageId = item.portfolioItem.media,
                    name = item.portfolioItem.name,
                    tags = item.tags.map{ tag -> tag.name }
                )
            }
        }
    }

    fun deletePortfolioItem(portfolioItemId: Long) {
        portfolioDao.deletePortfolioItem(portfolioItemId)
    }
}

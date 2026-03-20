package com.h0uss.aimart.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.h0uss.aimart.data.dao.ProductDao
import com.h0uss.aimart.data.mapper.toUserProductCardData
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.ProductData
import com.h0uss.aimart.data.model.UserProductCardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
class ProductRepository(
    private val productDao: ProductDao
) {

    fun getProductsPagingData(): Flow<PagingData<ProductCardData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                productDao.getProductsPagingSource()
            }
        ).flow
    }

    fun getProductsByUserId(userId: Long): Flow<List<UserProductCardData>> {
        return productDao.getProductsByUserId(userId).map { list ->
            list.map { it.toUserProductCardData() }
        }
    }

    fun getProductById(productId: Long): Flow<ProductData> {
        return productDao.getProductById(productId)
    }

    fun getProductByStringInside(string: String): Flow<PagingData<ProductCardData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                productDao.getProductByStringInside(string)
            }
        ).flow
    }
}

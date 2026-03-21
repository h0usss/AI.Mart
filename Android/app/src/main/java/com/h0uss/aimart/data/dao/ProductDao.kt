package com.h0uss.aimart.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.ProductData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductEntity>): List<Long>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity): Long

    @Query("""
        SELECT 
            p.id AS id,
            u.name AS authorName,
            p.name AS name,
            p.price AS price,
            p.images AS imagesUrl,
            p.product_status AS status,
            p.description AS description
        FROM product AS p
        JOIN user AS u ON p.user_id = u.id
        WHERE p.product_status = 'ACTIVE' and u.id != :withoutUserId
        ORDER BY p.create_date DESC
    """)
    fun getProductsPagingSource(withoutUserId: Long): PagingSource<Int, ProductCardData>

    @Query("""
        SELECT * 
        FROM product
        WHERE user_id = :userId
        ORDER BY create_date DESC
    """)
    fun getProductsByUserId(userId: Long): Flow<List<ProductEntity>>

    @Query("""
        SELECT 
            p.id AS productId,
            u.id AS userId,
            u.name AS author,
            p.name AS name,
            p.price AS price,
            p.images AS imagesUrl,
            p.product_status AS status
        FROM product AS p
        JOIN user AS u ON p.user_id = u.id
        WHERE p.id = :productId
    """)
    fun getProductById(productId: Long): Flow<ProductData>

    @Query("""
        SELECT 
            p.id AS id,
            u.name AS authorName,
            p.name AS name,
            p.price AS price,
            p.images AS imagesUrl,
            p.product_status AS status,
            p.description AS description
        FROM product AS p
        LEFT JOIN user AS u ON p.user_id = u.id
        WHERE p.product_status = 'ACTIVE' AND 
            (COALESCE(p.name, '') LIKE '%' || :string || '%' OR COALESCE(u.name, '') LIKE '%' || :string || '%')
        ORDER BY p.create_date DESC
    """)
    fun getProductByStringInside(string: String):  PagingSource<Int, ProductCardData>
}
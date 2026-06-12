package com.h0uss.aimart.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.data.enum.ProductStatus
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.ProductData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductEntity>): List<Long>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity)

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductEntityById(productId: Long): ProductEntity?

    @Query("""
        SELECT 
            p.id AS id,
            u.id AS authorId,
            u.name AS authorName,
            p.name AS name,
            p.price AS price,
            p.images AS imagesUrl,
            p.description AS description,
            (SELECT COUNT(*) FROM orders AS o WHERE o.product_id = p.id AND o.status = 'COMPLETE') AS orderCount,
            u.rate AS authorRate
        FROM product AS p
        JOIN user AS u ON p.user_id = u.id
        WHERE p.product_status = 'ACTIVE' and u.id != :withoutUserId
        ORDER BY orderCount DESC, authorRate DESC, p.create_date DESC
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
            p.description AS description,
            p.images AS imagesUrl,
            p.product_status AS status,
            (SELECT COUNT(*) FROM orders AS o WHERE o.product_id = p.id AND o.status = 'COMPLETE') AS orderCount
        FROM product AS p
        JOIN user AS u ON p.user_id = u.id
        WHERE p.id = :productId
    """)
    fun getProductById(productId: Long): Flow<ProductData>

    @Query("""
        SELECT 
            p.id AS id,
            u.id AS authorId,
            u.name AS authorName,
            p.name AS name,
            p.price AS price,
            p.images AS imagesUrl,
            p.description AS description,
            (SELECT COUNT(*) FROM orders AS o WHERE o.product_id = p.id AND o.status = 'COMPLETE') AS orderCount,
            u.rate AS authorRate
        FROM product AS p
        LEFT JOIN user AS u ON p.user_id = u.id
        WHERE p.product_status = 'ACTIVE' AND 
            (COALESCE(p.name, '') LIKE '%' || :string || '%' OR COALESCE(u.name, '') LIKE '%' || :string || '%')
        ORDER BY orderCount DESC, authorRate DESC, p.create_date DESC
    """)
    fun getProductByStringInside(string: String):  PagingSource<Int, ProductCardData>

    @Query("SELECT COALESCE(SUM(view_count), 0) FROM product WHERE user_id = :sellerId")
    fun getTotalViewCountBySellerId(sellerId: Long): Flow<Long>

    @Query("UPDATE product SET view_count = view_count + 1 WHERE id = :productId")
    suspend fun incrementViewCount(productId: Long)

    @Query("UPDATE product SET product_status = :status WHERE id = :productId")
    suspend fun updateProductStatus(productId: Long, status: ProductStatus)
}
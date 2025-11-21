package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.h0uss.aimart.data.emun.ProductStatus
import java.time.LocalDateTime

@Entity(
    tableName = "product"
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "id")               val id: Long = 0L,
    @ColumnInfo( name = "name")             val name: String,
    @ColumnInfo( name = "image")            val imageId: Int,
    @ColumnInfo( name = "price")            val price: Float,
    @ColumnInfo( name = "description")      val description: String,
    @ColumnInfo( name = "create_date")      val createDate: LocalDateTime,
    @ColumnInfo( name = "product_status")   val productStatus: ProductStatus,

    @ColumnInfo( name = "user_id")      val userId: Long,
)

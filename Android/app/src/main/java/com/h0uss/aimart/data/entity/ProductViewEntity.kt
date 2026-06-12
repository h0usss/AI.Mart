package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "product_view"
)
data class ProductViewEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "viewed_at") val viewedAt: LocalDateTime,
)

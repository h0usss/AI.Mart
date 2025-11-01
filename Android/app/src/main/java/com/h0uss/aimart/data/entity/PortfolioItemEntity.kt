package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "portfolio_item"
)
data class PortfolioItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long,

    @ColumnInfo(name = "price")         val price: Int,
    @ColumnInfo(name = "media")         val media: List<Int>,
    @ColumnInfo(name = "description")   val description: String,
    @ColumnInfo(name = "create_time")   val createTime: LocalDateTime,

    @ColumnInfo(name = "user_id")       val userId: Long,
)

package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "portfolio_tag",
    primaryKeys = ["tag_id", "portfolio_id"]
)
data class PortfolioTagEntity(
    @ColumnInfo(name = "tag_id")        val tagId: Long,
    @ColumnInfo(name = "portfolio_id")  val portfolioId: Long,
)

package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

@Entity(
    tableName = "portfolio_item"
)
data class PortfolioItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long = 0L,

    @ColumnInfo(name = "price")         val price: Float,
    @ColumnInfo(name = "media")         val media: Int,
    @ColumnInfo(name = "name")          val name: String,
    @ColumnInfo(name = "description")   val description: String,
    @ColumnInfo(name = "create_time")   val createTime: LocalDateTime,

    @ColumnInfo(name = "user_id")       val userId: Long
)

data class PortfolioItemWithTags(
    @Embedded
    val portfolioItem: PortfolioItemEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PortfolioTagEntity::class,
            parentColumn = "portfolio_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<TagEntity>
)

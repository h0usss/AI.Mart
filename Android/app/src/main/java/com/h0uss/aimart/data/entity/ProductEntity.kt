package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "product"
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "id")           val id: Long,
    @ColumnInfo( name = "name")         val name: String,
    @ColumnInfo( name = "media")        val media: List<Int>,
    @ColumnInfo( name = "price")        val price: BigDecimal,
    @ColumnInfo( name = "description")  val description: String,
    @ColumnInfo( name = "create_date")  val createDate: LocalDateTime,

    @ColumnInfo( name = "user_id")      val userId: Long,
)

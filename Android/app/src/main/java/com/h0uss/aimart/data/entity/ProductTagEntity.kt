package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "product_tag",
    primaryKeys = ["tag_id", "product_id"]
)
data class ProductTagEntity(
    @ColumnInfo( name = "tag_id" )      val tagId: Long,
    @ColumnInfo( name = "product_id" )  val productId: Long,
)

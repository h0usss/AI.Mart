package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "feedback"
)
data class FeedbackEntity(
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )          val id: Long,

    @ColumnInfo( name = "text" )        val text: String,
    @ColumnInfo( name = "star_count" )  val starCount: Int,
    @ColumnInfo( name = "timestamp" )   val timestamp: LocalDateTime,

    @ColumnInfo( name = "order_id" )    val orderId: Long
)

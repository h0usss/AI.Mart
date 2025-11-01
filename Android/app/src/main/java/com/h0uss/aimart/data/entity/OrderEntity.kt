package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.h0uss.aimart.data.emun.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "order"
)
data class OrderEntity(
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )              val id: Long,

    @ColumnInfo( name = "price" )           val price: BigDecimal,
    @ColumnInfo( name = "status" )          val status: OrderStatus,
    @ColumnInfo( name = "description" )     val description: String,
    @ColumnInfo( name = "deadline" )        val deadline: LocalDateTime,
    @ColumnInfo( name = "start_date" )      val startDate: LocalDateTime,
    @ColumnInfo( name = "completion_date" ) val completionDate: LocalDateTime?,

    @ColumnInfo( name = "user_id" )         val userId: Long,
    @ColumnInfo( name = "product_id" )      val productId: Long,
)

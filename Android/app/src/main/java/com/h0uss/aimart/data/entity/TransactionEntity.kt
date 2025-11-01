package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.h0uss.aimart.data.emun.TransactionFlowType
import com.h0uss.aimart.data.emun.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transaction"
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "id" )          val id: Long,

    @ColumnInfo( name = "amount" )      val amount: BigDecimal,
    @ColumnInfo( name = "type" )        val type: TransactionType,
    @ColumnInfo( name = "timestamp" )   val timestamp: LocalDateTime,
    @ColumnInfo( name = "flow" )        val flow: TransactionFlowType,

    @ColumnInfo( name = "user_id" )     val userId: Long,
    @ColumnInfo( name = "order_id" )    val orderId: Long,
)

package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

@Entity(
    tableName = "feedback"
)
data class FeedbackEntity(
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )          val id: Long = 0L,

    @ColumnInfo( name = "text" )        val text: String,
    @ColumnInfo( name = "star_count" )  val starCount: Int,
    @ColumnInfo( name = "timestamp" )   val timestamp: LocalDateTime,

    @ColumnInfo( name = "order_id" )    val orderId: Long
)

@DatabaseView("""
    SELECT 
        f.*, 
        o.buyer_id, 
        o.seller_id 
    FROM feedback AS f
    INNER JOIN orders AS o ON f.order_id = o.id
""")
data class FeedbackWithUserReferenceView(
    @Embedded
    val feedback: FeedbackEntity,

    @ColumnInfo(name = "buyer_id")
    val buyerId: Long,

    @ColumnInfo(name = "seller_id")
    val sellerId: Long
)

data class FeedbackWithUser(
    @Embedded
    val feedbackView: FeedbackWithUserReferenceView,

    @Relation(
        parentColumn = "buyer_id",
        entityColumn = "id"
    )
    val user: UserEntity
)

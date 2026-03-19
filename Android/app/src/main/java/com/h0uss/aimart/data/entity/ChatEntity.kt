package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "chats"
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,

    @ColumnInfo( name = "f_user_id" )     val fUserId: Long,
    @ColumnInfo( name = "s_user_id" )     val sUserId: Long,
    @ColumnInfo( name = "product_id" )     val productId: Long,

    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
)

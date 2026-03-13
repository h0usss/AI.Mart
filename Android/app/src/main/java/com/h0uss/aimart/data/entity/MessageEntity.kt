package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "messages"
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,

    @ColumnInfo( name = "chat_id" )     val chatId: Long,
    @ColumnInfo( name = "sender_id" )     val senderId: Long,

    @ColumnInfo( name = "message" )     val message: String,

    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
)

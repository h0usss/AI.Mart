package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "search_hint",
    primaryKeys = ["text", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SearchHintEntity(

    @ColumnInfo(name = "text")      val text: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "user_id")   val userId: Long = 0L
)


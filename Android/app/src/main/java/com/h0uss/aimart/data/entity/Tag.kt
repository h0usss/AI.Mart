package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "tag"
)
data class Tag(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long,

    @ColumnInfo(name = "name")          val name: String,
    @ColumnInfo(name = "create_date")   val createDate: LocalDateTime

)

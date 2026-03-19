package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_sell_info"
)
data class UserSellInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long = 0L,

    @ColumnInfo(name = "about")         val about: String,
    @ColumnInfo(name = "skills")        val skills: List<String>,
    @ColumnInfo(name = "profession")    val profession: String,

    @ColumnInfo(name = "user_id")       val userId: Long,
)
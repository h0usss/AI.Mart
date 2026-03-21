package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "user"
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long = 0L,

    @ColumnInfo(name = "rate")          val rate: Float,
    @ColumnInfo(name = "avatar")        val avatar: String,
    @ColumnInfo(name = "name")          val name: String,
    @ColumnInfo(name = "email")         val email: String,
    @ColumnInfo(name = "balance")       val balance: Float,
    @ColumnInfo(name = "nick_name")     val nickName: String,
    @ColumnInfo(name = "is_seller")     val isSeller: Boolean,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
    @ColumnInfo(name = "register_date") val registerTime: LocalDateTime,
)

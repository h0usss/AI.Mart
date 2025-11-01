package com.h0uss.aimart.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "user"
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")            val id: Long,

    @ColumnInfo(name = "avatar")        val avatar: Int,
    @ColumnInfo(name = "name")          val name: String,
    @ColumnInfo(name = "email")         val email: String,
    @ColumnInfo(name = "nick_name")     val nickName: String,
    @ColumnInfo(name = "contacts")      val contacts: String,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
    @ColumnInfo(name = "token_balance") val tokenBalance: BigDecimal,
    @ColumnInfo(name = "register_date") val registerTime: LocalDateTime,
)

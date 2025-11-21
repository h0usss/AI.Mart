package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.h0uss.aimart.data.entity.UserSellInfoEntity

@Dao
interface UserSellInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserSellInfoEntity>): List<Long>
}
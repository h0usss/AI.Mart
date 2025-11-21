package com.h0uss.aimart.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Transaction
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.data.dao.UserDao
import com.h0uss.aimart.data.mapper.toUserEntity
import com.h0uss.aimart.data.mapper.toUserHomeData
import com.h0uss.aimart.data.mapper.toUserLoginData
import com.h0uss.aimart.data.model.SellerData
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.data.model.UserHomeData
import com.h0uss.aimart.data.model.UserLoginData
import com.h0uss.aimart.data.model.UserRegistrationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
class UserRepository(
    private val userDao: UserDao,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createUser(newUser: UserRegistrationData): Long{
        val user = toUserEntity(newUser)
        return userDao.create(user)
    }

    suspend fun getUserIsSeller(userId: Long): Boolean{
        return userDao.getUserIsSeller(userId)
    }

    fun getUserByIdFlow(userId: Long): Flow<UserData>{
        return userDao.getUserByIdFlow(userId)
    }

    fun getUserCountBuyFlow(userId: Long): Flow<Int?>{
        return userDao.getUserCountBuyFlow(userId)
    }

    fun getUserCountSellFlow(userId: Long): Flow<Int>{
        return userDao.getUserCountSellFlow(userId)
    }

    fun getSellerByIdFlow(userId: Long): Flow<SellerData>{
        return userDao.getSellerByIdFlow(userId)
    }

    suspend fun getUserByEmailOrNick(emailOrNick: String): UserLoginData? {
        return userDao.getUserByEmailOrNick(emailOrNick)?.toUserLoginData()
    }

    fun getSellerLimit(limit: Int): Flow<List<UserHomeData>> {
        return userDao.getSellerLimit(limit).map { userList ->
            userList.filter { userEntity -> userEntity.id != authUserIdLong }
                .map { userEntity ->
                userEntity.toUserHomeData()
            }

        }
    }

    suspend fun getBalance(userId: Long): Float? {
        return userDao.getBalance(userId)
    }

    fun getUsersByStringInside(string: String): Flow<List<UserHomeData>>{
        return userDao.getUsersByStringInside(string).map{ list ->
            list.filter { userEntity -> userEntity.id != authUserIdLong }
                .map{
                it.toUserHomeData()
            }
        }
    }

    @Transaction
    suspend fun updateSeller(user: SellerData){
        userDao.updateUserInfo(user.id, user.name, user.nick, user.imageId)
        userDao.updateSellerSellInfo(user.id, user.profession, user.about, user.skills)
    }

    suspend fun deleteUser(userId: Long){
        userDao.deleteById(userId)
    }
}

package com.h0uss.aimart.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.R
import com.h0uss.aimart.data.entity.UserEntity
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.data.model.UserHomeData
import com.h0uss.aimart.data.model.UserLoginData
import com.h0uss.aimart.data.model.UserRegistrationData
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun toUserEntity(user: UserRegistrationData): UserEntity{

    return UserEntity(
        avatar = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
        name = user.name,
        email = user.email,
        nickName = "user_${System.currentTimeMillis()}",
        isSeller = false,
        balance = 0.0f,
        rate = 0f,
        registerTime = LocalDateTime.now(),
        passwordHash = BCrypt.hashpw(
            user.password,
            BCrypt.gensalt(12)
        ),
    )
}

fun UserEntity.toUserLoginData(): UserLoginData{
    return UserLoginData(
        id = this.id,
        email = this.email,
        nick = this.nickName,
        passwordHash = this.passwordHash
    )
}

fun UserEntity.toUserHomeData(): UserHomeData{
    return UserHomeData(
        id = this.id,
        name = this.name,
        imageUrl = this.avatar
    )
}

fun UserEntity.toUserData(): UserData{
    return UserData(
        id = this.id,
        name = this.name,
        nick = this.nickName,
        imageUrl = this.avatar,
        rate = this.rate,
        balance = this.balance,
    )
}
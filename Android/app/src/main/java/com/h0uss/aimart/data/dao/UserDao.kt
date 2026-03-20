package com.h0uss.aimart.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.h0uss.aimart.data.entity.UserEntity
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.data.model.SellerData
import com.h0uss.aimart.data.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun create(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>): List<Long>

    @Update
    suspend fun update(user: UserEntity)

    @Query("""
        UPDATE user
        SET
            name = :name,
            nick_name = :nick,
            avatar = :imageId
        WHERE id = :id
    """)
    suspend fun updateUserInfo(id: Long, name: String, nick: String, imageId: Int)

    @Query("""
        UPDATE user_sell_info
        SET
            profession = :profession,
            about = :about,
            skills = :skills
        WHERE user_id = :id
    """)
    suspend fun updateSellerSellInfo(id: Long, profession: String, about: String, skills: List<String>)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteById(userId: Long)


    @Query("""
        SELECT
        u.id AS id,
        u.name AS name,
        u.nick_name AS nick,
        u.avatar AS imageId,
        u.rate AS rate,
        u.balance AS balance
        FROM user AS u
        LEFT JOIN orders AS o ON u.id = o.buyer_id 
        WHERE u.id = :userId
    """)
    fun getUserByIdFlow(userId: Long): Flow<UserData>

    @Query("""
        SELECT
        u.id AS id,
        u.name AS name,
        u.nick_name AS nick,
        u.avatar AS imageId,
        u.rate AS rate,
        u.balance AS balance
        FROM user AS u
        LEFT JOIN product AS p ON u.id = p.user_id 
        WHERE p.id = :productId
    """)
    fun getUserByProductId(productId: Long): Flow<UserData>

    @Query("""
        SELECT
            u.id AS id,
            u.name AS userName,
            p.images AS productImagesId
        FROM user AS u
        LEFT JOIN chats AS c ON u.id = c.f_user_id OR u.id = c.s_user_id 
        LEFT JOIN product AS p ON c.product_id = p.id
        WHERE c.id = :chatId
        GROUP BY u.id
    """)
    fun getUsersByChatId(chatId: Long): Flow<List<ChatUserData>>

    @Query("""
        SELECT u.is_seller
        FROM user AS u
        WHERE u.id = :userId
    """)
    suspend fun getUserIsSeller(userId: Long): Boolean

    @Query("""
        SELECT COUNT(o.id)
        FROM user AS u
        LEFT JOIN orders AS o ON u.id = o.buyer_id  
        WHERE u.id = :userId
    """)
    fun getUserCountBuyFlow(userId: Long): Flow<Int?>

    @Query("""
        SELECT COUNT(o.id)
        FROM user AS u
        LEFT JOIN orders AS o ON u.id = o.seller_id 
        WHERE u.id = :userId
    """)
    fun getUserCountSellFlow(userId: Long): Flow<Int>

    @Query("""
        SELECT
            u.id AS id,
            u.name AS name,
            u.balance AS balance,
            u.nick_name AS nick,
            u.avatar AS imageId,
            u.rate AS rate,
            COUNT(o.id) AS countSell,
            usi.profession AS profession,
            usi.about AS about,
            usi.skills AS skills
        FROM user AS u
        LEFT JOIN orders AS o ON u.id = o.seller_id 
        LEFT JOIN user_sell_info AS usi ON u.id = usi.user_id
        WHERE u.id = :userId
        GROUP BY u.id
    """)
    fun getSellerByIdFlow(userId: Long): Flow<SellerData?>

    @Query("""
        SELECT * 
        FROM user 
        WHERE email = :emailOrNick OR nick_name = :emailOrNick
    """)
    suspend fun getUserByEmailOrNick(emailOrNick: String): UserEntity?

    @Query(
        """
        SELECT * 
        FROM user 
        WHERE is_seller = 1
        LIMIT :limit
    """
    )
    fun getSellerLimit(limit: Int): Flow<List<UserEntity>>

    @Query("""
        SELECT balance 
        FROM user 
        WHERE id = :userId
    """)
    suspend fun getBalance(userId: Long): Float?

    @Query("""
        SELECT u.* 
        FROM user AS u
        LEFT JOIN user_sell_info AS usi ON u.id = usi.user_id
        WHERE COALESCE(u.name, '') LIKE '%' || :string || '%'
           OR COALESCE(u.email, '') LIKE '%' || :string || '%'
           OR COALESCE(u.nick_name, '') LIKE '%' || :string || '%'
           OR COALESCE(usi.about, '') LIKE '%' || :string || '%'
           OR COALESCE(usi.skills, '') LIKE '%' || :string || '%'
           OR COALESCE(usi.profession, '') LIKE '%' || :string || '%'
    """)
    fun getUsersByStringInside(string: String): Flow<List<UserEntity>>
}
package com.h0uss.aimart

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.dataStore.SessionManager
import com.h0uss.aimart.data.database.AppDataBase
import com.h0uss.aimart.data.repository.ChatRepository
import com.h0uss.aimart.data.repository.FeedbackRepository
import com.h0uss.aimart.data.repository.MessageRepository
import com.h0uss.aimart.data.repository.OrderRepository
import com.h0uss.aimart.data.repository.PortfolioRepository
import com.h0uss.aimart.data.repository.ProductRepository
import com.h0uss.aimart.data.repository.SearchHintRepository
import com.h0uss.aimart.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
object Graph {
    val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var db: AppDataBase
        private set
    lateinit var session: SessionManager
        private set

    var authUserIdFlow = MutableStateFlow(-1L)
        private set

    var authUserIsSellerFlow = MutableStateFlow(false)
        private set


    val userRepository by lazy {
        UserRepository(
            userDao = db.userDao(),
        )
    }
    val productRepository by lazy {
        ProductRepository(
            productDao = db.productDao()
        )
    }
    val orderRepository by lazy {
        OrderRepository(
            orderDao = db.orderDao(),
        )
    }
    val searchHintRepository by lazy {
        SearchHintRepository(
            searchHintDao = db.searchHintDao(),
        )
    }
    val portfolioRepository by lazy {
        PortfolioRepository(
            portfolioDao = db.portfolioDao(),
        )
    }
    val feedbackRepository by lazy {
        FeedbackRepository(
            feedbackDao = db.feedbackDao(),
        )
    }
    val chatRepository by lazy {
        ChatRepository(
            chatDao = db.chatDao(),
        )
    }
    val messageRepository by lazy {
        MessageRepository(
            messageDao = db.messageDao(),
        )
    }


    val authUserIdLong: Long
        get() = authUserIdFlow.value

    suspend fun saveUserId(userId: Long) {
        if (!::session.isInitialized) {
            throw IllegalStateException("Graph must be initialized via provide() before calling saveUserId.")
        }
        session.saveUserId(userId)
        authUserIdFlow.value = userId
    }

    suspend fun deleteUserId() {
        if (!::session.isInitialized) {
            throw IllegalStateException("Graph must be initialized via provide() before calling saveUserId.")
        }
        session.deleteUserId()
        authUserIdFlow.value = -1L
    }

    val authUserIsSeller: Boolean
        get() = authUserIsSellerFlow.value

    suspend fun saveUserIsSeller(isSeller: Boolean) {
        if (!::session.isInitialized) {
            throw IllegalStateException("Graph must be initialized via provide() before calling saveUserId.")
        }
        session.saveUserIsSeller(isSeller)
        authUserIsSellerFlow.value = isSeller
    }

    suspend fun deleteUserIsSeller() {
        if (!::session.isInitialized) {
            throw IllegalStateException("Graph must be initialized via provide() before calling saveUserId.")
        }
        session.deleteUserIsSeller()
        authUserIsSellerFlow.value = false
    }

    fun provide(context: Context){
        db = AppDataBase.getDataBase(context,
            applicationScope
        )
        session = SessionManager().init(context.applicationContext)

        applicationScope.launch {
            val savedId = session.userId.firstOrNull()?.toLongOrNull() ?: -1L
            authUserIdFlow.value = savedId
        }
    }
}
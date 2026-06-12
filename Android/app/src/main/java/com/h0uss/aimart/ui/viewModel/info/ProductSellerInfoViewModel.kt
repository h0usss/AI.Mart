package com.h0uss.aimart.ui.viewModel.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.feedbackRepository
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.Graph.productViewDao
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.ProductData
import com.h0uss.aimart.data.model.UserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class ProductSellerInfoViewModel(
    private val productId: Long
) : ViewModel() {

    var state = MutableStateFlow(ProductSellerInfoState())
        private set

    var navigationEvents = Channel<ProductSellerInfoNavigationEvent>()
        private set

    init {
        viewModelScope.launch {
            val product = productRepository.getProductEntityById(productId)
            if (product != null && product.userId != authUserIdLong) {
                productRepository.incrementViewCountAndInsertView(productId, authUserIdLong)
            }
        }

        productRepository.getProductById(productId)
            .onEach { product ->
                state.update { it.copy(product = product) }
            }
            .launchIn(viewModelScope)

        val todayStart = LocalDate.now().atStartOfDay()
        val todayEnd = LocalDate.now().atTime(LocalTime.MAX)

        productViewDao.getTotalUniqueViewsByProductId(productId)
            .onEach { total ->
                state.update { it.copy(totalViews = total.toInt()) }
            }
            .launchIn(viewModelScope)

        productViewDao.getUniqueViewsByProductIdBetween(productId, todayStart, todayEnd)
            .onEach { today ->
                state.update { it.copy(todayViews = today.toInt()) }
            }
            .launchIn(viewModelScope)

        userRepository.getUserByProductId(productId)
            .onEach { user ->
                state.update { it.copy(user = user) }
            }
            .flatMapLatest { user ->
                feedbackRepository.getFeedbackBySellerIdFlow(user.id)
            }
            .onEach { feedback ->
                val initialFilteredFeedback = feedback.sortedBy { it.starCount }
                state.update {
                    it.copy(
                        originalFeedback = feedback,
                        filteredFeedback = initialFilteredFeedback,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ProductSellerInfoEvent) {
        when (event) {
            is ProductSellerInfoEvent.UserClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ProductSellerInfoNavigationEvent.User(event.value))
                }
            }

            is ProductSellerInfoEvent.FeedbackTagClick -> {
                handleFeedbackTagClick(event.index)
            }

            is ProductSellerInfoEvent.EditClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ProductSellerInfoNavigationEvent.EditProduct)
                }
            }
        }
    }

    private fun handleFeedbackTagClick(index: Int) {
        val currentState = state.value
        val newFeedbackFilter = MutableList(3) { false }
        newFeedbackFilter[index] = true

        val newFilteredFeedbackList = when (index) {
            0 -> currentState.originalFeedback.sortedBy { it.starCount }
            1 -> currentState.originalFeedback
                .filter { it.starCount >= 3 }
                .sortedByDescending { it.starCount }

            2 -> currentState.originalFeedback
                .filter { it.starCount < 3 }
                .sortedBy { it.starCount }

            else -> currentState.originalFeedback.sortedBy { it.starCount }
        }

        state.update {
            it.copy(
                feedbackFilter = newFeedbackFilter,
                filteredFeedback = newFilteredFeedbackList
            )
        }
    }
}

data class ProductSellerInfoState(
    val product: ProductData = ProductData(),

    val user: UserData = UserData(),

    val feedbackFilter: List<Boolean> = listOf(true, false, false),
    val filteredFeedback: List<FeedbackData> = listOf(),
    val originalFeedback: List<FeedbackData> = listOf(),

    val totalViews: Int = 0,
    val todayViews: Int = 0,
)

sealed class ProductSellerInfoEvent {
    data class UserClick(val value: Long) : ProductSellerInfoEvent()
    data class FeedbackTagClick(val index: Int) : ProductSellerInfoEvent()
    data object EditClick : ProductSellerInfoEvent()
}

sealed class ProductSellerInfoNavigationEvent {
    data class User(val value: Long) : ProductSellerInfoNavigationEvent()
    data object EditProduct : ProductSellerInfoNavigationEvent()
}

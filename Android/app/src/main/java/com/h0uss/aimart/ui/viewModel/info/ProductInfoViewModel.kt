package com.h0uss.aimart.ui.viewModel.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.feedbackRepository
import com.h0uss.aimart.Graph.productRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class ProductInfoViewModel(
    private val productId: Long
) : ViewModel(){

    var state = MutableStateFlow(ProductInfoState())
        private set

    var navigationEvents = Channel<ProductInfoNavigationEvent>()
        private set

    init {
        productRepository.getProductById(productId)
            .onEach { product ->
                state.update { it.copy(product = product) }
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

    fun onEvent(event: ProductInfoEvent) {
        when(event){
            is ProductInfoEvent.UserClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ProductInfoNavigationEvent.User(event.value))
                }
            }
            is ProductInfoEvent.FeedbackTagClick -> {
                handleFeedbackTagClick(event.index)
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

data class ProductInfoState(
    val product: ProductData = ProductData(),

    val user: UserData = UserData(),

    val feedbackFilter: List<Boolean> = listOf(true, false, false),
    val filteredFeedback: List<FeedbackData> = listOf(),
    val originalFeedback: List<FeedbackData> = listOf(),
)

sealed class ProductInfoEvent {
    data class UserClick(val value: Long) : ProductInfoEvent()
    data class FeedbackTagClick(val index: Int) : ProductInfoEvent()
}

sealed class ProductInfoNavigationEvent {
    data class User(val value: Long) : ProductInfoNavigationEvent()
}

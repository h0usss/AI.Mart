package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.chatRepository
import com.h0uss.aimart.Graph.feedbackRepository
import com.h0uss.aimart.Graph.portfolioRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.entity.ChatEntity
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.data.model.SellerData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class SellerProfileForUserViewModel(
    private val userId: Long
) : ViewModel() {

    var state = MutableStateFlow(SellerProfileForUserState())
        private set

    var navigationEvents = Channel<SellerProfileForUserNavigationEvent>()
        private set

    init {
        combine(
            userRepository.getSellerByIdFlow(userId),
            userRepository.getUserCountSellFlow(userId),
            portfolioRepository.getPortfolioBySellerIdFlow(userId),
            feedbackRepository.getFeedbackBySellerIdFlow(userId)
        ) { seller, countSell, portfolio, feedback ->
            val allTags = listOf("Все") + portfolio.flatMap { it.tags }.distinct()
            val initialFilteredFeedback = feedback.sortedBy { it.starCount }
            if (seller != null)
                state.update {
                    it.copy(
                        user = seller,
                        countSell = countSell,
                        originalPortfolio = portfolio,
                        portfolio = portfolio,
                        originalFeedback = feedback,
                        filteredFeedback = initialFilteredFeedback,
                        allPortfolioTags = allTags,
                        portfolioTagFilter = List(allTags.size) { index -> index == 0 }
                    )
                }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SellerProfileForUserEvent) {
        when (event) {
            is SellerProfileForUserEvent.BackClick -> {
                sendNavEvent(SellerProfileForUserNavigationEvent.BackClick)
            }
            is SellerProfileForUserEvent.AdditionalItemClick -> {
                when (event.id) {
                    "share" -> {}
                    "complaint" -> {}
                    "block" -> {}
                }
            }
            is SellerProfileForUserEvent.ShowAdditionalMenu -> {
                state.update { it.copy(isShowAdditional = true) }
            }
            is SellerProfileForUserEvent.DismissAdditionalMenu -> {
                state.update { it.copy(isShowAdditional = false) }
            }
            is SellerProfileForUserEvent.LikeClick -> {
                state.update { it.copy(isLike = !state.value.isLike) }
            }
            is SellerProfileForUserEvent.WriteClick -> {
                viewModelScope.launch {
                    val sellerId = state.value.user.id
                    val chat = chatRepository.getChatNoProduct(authUserIdLong, sellerId).firstOrNull()
                    
                    if (chat != null) {
                        sendNavEvent(SellerProfileForUserNavigationEvent.WriteClick(chat.id))
                    } else {
                        val newChatId = chatRepository.insert(
                            ChatEntity(
                                fUserId = authUserIdLong,
                                sUserId = sellerId,
                                orderId = null,
                                createdAt = LocalDateTime.now(),
                            )
                        )
                        sendNavEvent(SellerProfileForUserNavigationEvent.WriteClick(newChatId))
                    }
                }
            }
            is SellerProfileForUserEvent.PortfolioTagClick -> {
                handlePortfolioTagClick(event.name)
            }
            is SellerProfileForUserEvent.FeedbackTagClick -> {
                handleFeedbackTagClick(event.index)
            }
            is SellerProfileForUserEvent.ShowPortfolioItem -> {
                viewModelScope.launch {
                    val portfolioItem = portfolioRepository
                        .getPortfolioByIdFlow(event.portfolioId)
                        .first()

                    sendNavEvent(SellerProfileForUserNavigationEvent.ShowPortfolioItem(portfolioItem))
                }
            }
        }
    }

    private fun handlePortfolioTagClick(tagName: String) {
        val currentState = state.value
        val clickedTagIndex = currentState.allPortfolioTags.indexOf(tagName)
        if (clickedTagIndex == -1) return

        val newFilterState = currentState.portfolioTagFilter.toMutableList()
        val isAllTagClicked = clickedTagIndex == 0

        if (isAllTagClicked) {
            newFilterState.fill(false)
            newFilterState[0] = true
        } else {
            newFilterState[clickedTagIndex] = !newFilterState[clickedTagIndex]
            newFilterState[0] = false

            val anyOtherTagSelected = newFilterState.subList(1, newFilterState.size).any { it }
            if (!anyOtherTagSelected) {
                newFilterState[0] = true
            }
        }

        val newPortfolioList = if (newFilterState[0]) {
            currentState.originalPortfolio
        } else {
            val selectedTagNames = currentState.allPortfolioTags
                .filterIndexed { index, _ -> newFilterState[index] }
                .toSet()

            currentState.originalPortfolio.filter { portfolioItem ->
                portfolioItem.tags.any { tag -> tag in selectedTagNames }
            }
        }

        state.update {
            it.copy(
                portfolioTagFilter = newFilterState,
                portfolio = newPortfolioList
            )
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
                feedbackTagFilter = newFeedbackFilter,
                filteredFeedback = newFilteredFeedbackList
            )
        }
    }

    private fun sendNavEvent(event: SellerProfileForUserNavigationEvent) {
        viewModelScope.launch { navigationEvents.send(event) }
    }
}


data class SellerProfileForUserState(
    val user: SellerData = SellerData(),
    val isShowAdditional: Boolean = false,
    val isLike: Boolean = false,
    val countSell: Int = 0,

    val allPortfolioTags: List<String> = listOf("Все"),
    val portfolioTagFilter: List<Boolean> = listOf(true),

    val feedbackTagFilter: List<Boolean> = listOf(true, false, false),

    val portfolio: List<PortfolioItemData> = listOf(),
    val originalPortfolio: List<PortfolioItemData> = listOf(),

    val filteredFeedback: List<FeedbackData> = listOf(),
    val originalFeedback: List<FeedbackData> = listOf(),
)

sealed class SellerProfileForUserEvent {
    object BackClick : SellerProfileForUserEvent()
    object ShowAdditionalMenu : SellerProfileForUserEvent()
    object DismissAdditionalMenu : SellerProfileForUserEvent()
    object LikeClick : SellerProfileForUserEvent()
    object WriteClick : SellerProfileForUserEvent()
    data class AdditionalItemClick(val id: String) : SellerProfileForUserEvent()
    data class PortfolioTagClick(val name: String) : SellerProfileForUserEvent()
    data class FeedbackTagClick(val index: Int) : SellerProfileForUserEvent()
    data class ShowPortfolioItem(val portfolioId: Long) : SellerProfileForUserEvent()
}

sealed class SellerProfileForUserNavigationEvent {
    object BackClick : SellerProfileForUserNavigationEvent()
    data class WriteClick(val id: Long) : SellerProfileForUserNavigationEvent()
    data class ShowPortfolioItem(val portfolioItem: PortfolioItemData) : SellerProfileForUserNavigationEvent()
}

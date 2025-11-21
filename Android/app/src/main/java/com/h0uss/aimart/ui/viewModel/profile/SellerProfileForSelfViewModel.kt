package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.feedbackRepository
import com.h0uss.aimart.Graph.portfolioRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.data.model.SellerData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SellerProfileForSelfViewModel : ViewModel(){

    var state = MutableStateFlow(SellerProfileForSelfState())
        private set

    var navigationEvents = Channel<SellerProfileForSelfNavigationEvent>()
        private set

    init{
        combine(
            userRepository.getSellerByIdFlow(authUserIdLong),
            userRepository.getUserCountSellFlow(authUserIdLong),
            portfolioRepository.getPortfolioBySellerIdFlow(authUserIdLong),
            feedbackRepository.getFeedbackBySellerIdFlow(authUserIdLong)
        ) { seller, countSell, portfolio, feedback ->
            val allTags = listOf("Все") + portfolio.flatMap { it.tags }.distinct()
            val initialFilteredFeedback = feedback.sortedBy { it.starCount }

            state.update {
                it.copy(
                    user = seller,
                    countSell = countSell,
                    originalPortfolio = portfolio,
                    portfolio = portfolio,
                    originalFeedback = feedback,
                    filteredFeedback = initialFilteredFeedback,
                    allPortfolioTags = allTags,
                    portfolioFilter = List(allTags.size) { index -> index == 0 }
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SellerProfileForSelfEvent) {
        when(event){
            is SellerProfileForSelfEvent.ShowSettingsMenu -> {
                state.update {
                    it.copy(isVisibleSettings = true)
                }
            }
            is SellerProfileForSelfEvent.DismissSettingsMenu -> {
                state.update {
                    it.copy(isVisibleSettings = false)
                }
            }
            is SellerProfileForSelfEvent.EditClick -> {
                sendNavEvent(SellerProfileForSelfNavigationEvent.EditClick)
            }
            is SellerProfileForSelfEvent.ReplenishAccountClick -> {
                sendNavEvent(SellerProfileForSelfNavigationEvent.ReplenishAccountClick)
            }
            is SellerProfileForSelfEvent.EmptiedAccountClick -> {
                sendNavEvent(SellerProfileForSelfNavigationEvent.EmptiedAccountClick)
            }
            is SellerProfileForSelfEvent.AddCaseClick -> {
                sendNavEvent(SellerProfileForSelfNavigationEvent.AddCaseClick)
            }
            is SellerProfileForSelfEvent.DeleteCaseClick -> {
                viewModelScope.launch {
                    portfolioRepository.deletePortfolioItem(event.id)
                }
            }
            is SellerProfileForSelfEvent.PortfolioItemClick -> {
                sendNavEvent(SellerProfileForSelfNavigationEvent.PortfolioItemClick(event.id))
            }
            is SellerProfileForSelfEvent.PortfolioTagClick -> {
                handlePortfolioTagClick(event.name)
            }
            is SellerProfileForSelfEvent.FeedbackTagClick -> {
                handleFeedbackTagClick(event.index)
            }
        }
    }

    private fun handlePortfolioTagClick(tagName: String) {
        val currentState = state.value
        val clickedTagIndex = currentState.allPortfolioTags.indexOf(tagName)
        if (clickedTagIndex == -1) return

        val newFilterState = currentState.portfolioFilter.toMutableList()
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
                portfolioFilter = newFilterState,
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
                feedbackFilter = newFeedbackFilter,
                filteredFeedback = newFilteredFeedbackList
            )
        }
    }

    private fun sendNavEvent(event: SellerProfileForSelfNavigationEvent) {
        viewModelScope.launch { navigationEvents.send(event) }
    }
}

data class SellerProfileForSelfState(
    val user: SellerData = SellerData(),
    val isVisibleSettings: Boolean = false,
    val countSell: Int = 0,

    val allPortfolioTags: List<String> = listOf("Все"),
    val portfolioFilter: List<Boolean> = listOf(true),

    val feedbackFilter: List<Boolean> = listOf(true, false, false),

    val portfolio: List<PortfolioItemData> = listOf(),
    val originalPortfolio: List<PortfolioItemData> = listOf(),

    val filteredFeedback: List<FeedbackData> = listOf(),
    val originalFeedback: List<FeedbackData> = listOf(),
)

sealed class SellerProfileForSelfEvent {
    object ShowSettingsMenu : SellerProfileForSelfEvent()
    object DismissSettingsMenu : SellerProfileForSelfEvent()
    object EditClick: SellerProfileForSelfEvent()
    object ReplenishAccountClick : SellerProfileForSelfEvent()
    object EmptiedAccountClick : SellerProfileForSelfEvent()
    object AddCaseClick : SellerProfileForSelfEvent()
    data class DeleteCaseClick(val id: Long) : SellerProfileForSelfEvent()
    data class PortfolioTagClick(val name: String) : SellerProfileForSelfEvent()
    data class PortfolioItemClick(val id: Long) : SellerProfileForSelfEvent()
    data class FeedbackTagClick(val index: Int) : SellerProfileForSelfEvent()
}

sealed class SellerProfileForSelfNavigationEvent {
    object EditClick : SellerProfileForSelfNavigationEvent()
    object ReplenishAccountClick : SellerProfileForSelfNavigationEvent()
    object EmptiedAccountClick : SellerProfileForSelfNavigationEvent()
    object AddCaseClick : SellerProfileForSelfNavigationEvent()
    data class PortfolioItemClick(val id: Long) : SellerProfileForSelfNavigationEvent()
}

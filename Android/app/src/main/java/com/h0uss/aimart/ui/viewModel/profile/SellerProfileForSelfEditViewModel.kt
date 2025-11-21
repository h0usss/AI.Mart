package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.deleteUserId
import com.h0uss.aimart.Graph.portfolioRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.data.model.SellerData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SellerProfileForSelfEditViewModel : ViewModel() {

    var state = MutableStateFlow(SellerProfileForSelfEditState())
        private set

    var navigationEvents = Channel<SellerProfileForSelfEditNavigationEvent>()
        private set

    init {
        viewModelScope.launch {
            val user = userRepository.getSellerByIdFlow(authUserIdLong).firstOrNull()
            val portfolio = portfolioRepository.getPortfolioBySellerIdFlow(authUserIdLong).firstOrNull() ?: emptyList()

            if (user != null) {
                val allTags = listOf("Все") + portfolio.flatMap { it.tags }.distinct()
                state.update {
                    it.copy(
                        user = user,
                        originalPortfolio = portfolio,
                        portfolio = portfolio,
                        allPortfolioTags = allTags,
                        portfolioFilter = List(allTags.size) { index -> index == 0 }
                    )
                }
            }
        }
    }

    fun onEvent(event: SellerProfileForSelfEditEvent) {
        when (event) {
            is SellerProfileForSelfEditEvent.SaveClick -> {
                viewModelScope.launch {
                    userRepository.updateSeller(state.value.user)
                    sendNavEvent(SellerProfileForSelfEditNavigationEvent.SaveClick)
                }
            }
            is SellerProfileForSelfEditEvent.ExitClick -> {
                viewModelScope.launch {
                    deleteUserId()
                    sendNavEvent(SellerProfileForSelfEditNavigationEvent.ExitClick)
                }
            }
            is SellerProfileForSelfEditEvent.DeleteAccountClick -> {
                viewModelScope.launch {
                    userRepository.deleteUser(authUserIdLong)
                    deleteUserId()
                    sendNavEvent(SellerProfileForSelfEditNavigationEvent.DeleteAccountClick)
                }
            }
            is SellerProfileForSelfEditEvent.AboutChange -> {
                state.update {
                    it.copy(user = it.user.copy(about = event.about))
                }
            }
            is SellerProfileForSelfEditEvent.SkillChange -> {
                state.update { it.copy(newSkillValue = event.newSkill) }
            }
            is SellerProfileForSelfEditEvent.AddSkillClick -> {
                if (state.value.newSkillValue.isNotBlank()) {
                    val newSkills = state.value.user.skills + state.value.newSkillValue
                    state.update {
                        it.copy(
                            user = it.user.copy(skills = newSkills),
                            newSkillValue = ""
                        )
                    }
                }
            }
            is SellerProfileForSelfEditEvent.DeleteCaseClick -> {
                viewModelScope.launch {
                    portfolioRepository.deletePortfolioItem(event.id)

                    state.update {
                        val newOriginalPortfolio = it.originalPortfolio.filter { item -> item.id != event.id }
                        val newFilteredPortfolio = it.portfolio.filter { item -> item.id != event.id }
                        it.copy(
                            originalPortfolio = newOriginalPortfolio,
                            portfolio = newFilteredPortfolio
                        )
                    }
                }
            }
            is SellerProfileForSelfEditEvent.PortfolioTagClick -> {
                handlePortfolioTagClick(event.name)
            }
            is SellerProfileForSelfEditEvent.ShowSettingsMenu -> {
                state.update { it.copy(isVisibleSettings = true) }
            }
            is SellerProfileForSelfEditEvent.DismissSettingsMenu -> {
                state.update { it.copy(isVisibleSettings = false) }
            }
            is SellerProfileForSelfEditEvent.AddCaseClick -> {
                sendNavEvent(SellerProfileForSelfEditNavigationEvent.AddCaseClick)
            }
            is SellerProfileForSelfEditEvent.PortfolioItemClick -> {
                sendNavEvent(SellerProfileForSelfEditNavigationEvent.PortfolioItemClick(event.id))
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
            if (newFilterState.subList(1, newFilterState.size).none { it }) {
                newFilterState[0] = true
            }
        }

        val newPortfolioList = if (newFilterState[0]) {
            currentState.originalPortfolio
        } else {
            val selectedTagNames = currentState.allPortfolioTags
                .filterIndexed { index, _ -> newFilterState[index] }
                .toSet()
            currentState.originalPortfolio.filter { item -> item.tags.any { it in selectedTagNames } }
        }

        state.update {
            it.copy(
                portfolioFilter = newFilterState,
                portfolio = newPortfolioList
            )
        }
    }

    private fun sendNavEvent(event: SellerProfileForSelfEditNavigationEvent) {
        viewModelScope.launch { navigationEvents.send(event) }
    }
}

data class SellerProfileForSelfEditState(
    val user: SellerData = SellerData(),
    val newSkillValue: String = "",
    val isVisibleSettings: Boolean = false,
    val allPortfolioTags: List<String> = listOf("Все"),
    val portfolioFilter: List<Boolean> = listOf(true),

    val portfolio: List<PortfolioItemData> = listOf(),
    val originalPortfolio: List<PortfolioItemData> = listOf()
)

sealed class SellerProfileForSelfEditEvent {
    object SaveClick : SellerProfileForSelfEditEvent()
    object ExitClick : SellerProfileForSelfEditEvent()
    object DeleteAccountClick : SellerProfileForSelfEditEvent()
    object ShowSettingsMenu : SellerProfileForSelfEditEvent()
    object DismissSettingsMenu : SellerProfileForSelfEditEvent()
    object AddCaseClick : SellerProfileForSelfEditEvent()
    data class AddSkillClick(val newSkill: String) : SellerProfileForSelfEditEvent()
    data class AboutChange(val about: String) : SellerProfileForSelfEditEvent()
    data class SkillChange(val newSkill: String) : SellerProfileForSelfEditEvent()
    data class DeleteCaseClick(val id: Long) : SellerProfileForSelfEditEvent()
    data class PortfolioTagClick(val name: String) : SellerProfileForSelfEditEvent()
    data class PortfolioItemClick(val id: Long) : SellerProfileForSelfEditEvent()
}

sealed class SellerProfileForSelfEditNavigationEvent {
    object SaveClick : SellerProfileForSelfEditNavigationEvent()
    object ExitClick : SellerProfileForSelfEditNavigationEvent()
    object DeleteAccountClick : SellerProfileForSelfEditNavigationEvent()
    object AddCaseClick : SellerProfileForSelfEditNavigationEvent()
    data class PortfolioItemClick(val id: Long) : SellerProfileForSelfEditNavigationEvent()
}

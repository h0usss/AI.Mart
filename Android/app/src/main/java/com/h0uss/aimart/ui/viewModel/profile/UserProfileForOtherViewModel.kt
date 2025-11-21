package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class UserProfileForOtherViewModel(
    private val userId: Long
) : ViewModel(){

    var state = MutableStateFlow(UserProfileForOtherState())
        private set

    var navigationEvents = Channel<UserProfileForOtherNavigationEvent>()
        private set

    init {
        viewModelScope.launch {
            userRepository.getUserByIdFlow(userId)
                .onEach { user ->
                    if (user != null) {
                        state.update {
                            it.copy(user = user)
                        }
                    }
                }
                .launchIn(viewModelScope)

            userRepository.getUserCountBuyFlow(userId)
                .onEach { count ->
                    state.update {
                        it.copy(countBuy = count ?: 0)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onEvent(event: UserProfileForOtherEvent) {
        when(event){
            is UserProfileForOtherEvent.BackClick -> {
                viewModelScope.launch {
                    navigationEvents.send(UserProfileForOtherNavigationEvent.BackClick)
                }
            }
            is UserProfileForOtherEvent.ShowAdditionalMenu -> {
                state.update {
                    it.copy(isShowAdditional = true)
                }
            }
            is UserProfileForOtherEvent.DismissAdditionalMenu -> {
                state.update {
                    it.copy(isShowAdditional = false)
                }
            }
            is UserProfileForOtherEvent.AdditionalItemClick -> {
                when(event.id){
                    "share" -> {}
                    "complaint" -> {}
                    "block" -> {}
                    else -> {}
                }
            }
        }
    }
}

data class UserProfileForOtherState(
    val user: UserData = UserData(),
    val isShowAdditional: Boolean = false,
    val countBuy: Int = 0,
)

sealed class UserProfileForOtherEvent {
    data class AdditionalItemClick(val id: String) : UserProfileForOtherEvent()
    object ShowAdditionalMenu : UserProfileForOtherEvent()
    object DismissAdditionalMenu : UserProfileForOtherEvent()
    object BackClick : UserProfileForOtherEvent()
}

sealed class UserProfileForOtherNavigationEvent {
    object BackClick : UserProfileForOtherNavigationEvent()
}
package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class UserProfileForSelfViewModel : ViewModel(){

    var state = MutableStateFlow(UserProfileForSelfState())
    private set

    var navigationEvents = Channel<UserProfileForSelfNavigationEvent>()
    private set

    init{
        viewModelScope.launch {
            userRepository.getUserByIdFlow(authUserIdLong)
                .onEach { user ->
                    if (user != null) {
                        state.update {
                            it.copy(user = user)
                        }
                    }
                }
                .launchIn(viewModelScope)

            userRepository.getUserCountBuyFlow(authUserIdLong)
                .onEach { count ->
                    state.update {
                        it.copy(countBuy = count ?: 0)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onEvent(event: UserProfileForSelfEvent) {
        when(event){
            is UserProfileForSelfEvent.EditClick -> {
                viewModelScope.launch {
                    navigationEvents.send(UserProfileForSelfNavigationEvent.EditClick(event.id))
                }
            }
            is UserProfileForSelfEvent.ShowSettingsMenu -> {
                state.update {
                    it.copy(isVisibleSettings = true)
                }
            }
            is UserProfileForSelfEvent.DismissSettingsMenu -> {
                state.update {
                    it.copy(isVisibleSettings = false)
                }
            }
            is UserProfileForSelfEvent.ReplenishAccount -> {

            }
            is UserProfileForSelfEvent.EmptiedAccount -> {

            }
            is UserProfileForSelfEvent.SettingItemClick -> {
                when(event.id){
                    "profile" -> {}
                    "like" -> {}
                    "lock" -> {}
                    "notifications" -> {}
                    "globe" -> {}
                    "question" -> {}
                    else -> {}
                }
            }
        }
    }
}

data class UserProfileForSelfState(
    val user: UserData = UserData(),
    val isVisibleSettings: Boolean = false,
    val countBuy: Int = 0,
)

sealed class UserProfileForSelfEvent {
    data class EditClick(val id: Long) : UserProfileForSelfEvent()
    data class SettingItemClick(val id: String) : UserProfileForSelfEvent()
    object ShowSettingsMenu: UserProfileForSelfEvent()
    object DismissSettingsMenu: UserProfileForSelfEvent()
    object ReplenishAccount: UserProfileForSelfEvent()
    object EmptiedAccount: UserProfileForSelfEvent()
}

sealed class UserProfileForSelfNavigationEvent {
    data class EditClick(val id: Long) : UserProfileForSelfNavigationEvent()
    data class ReplenishAccount(val id: Long) : UserProfileForSelfNavigationEvent()
    data class EmptiedAccount(val id: Long) : UserProfileForSelfNavigationEvent()
}
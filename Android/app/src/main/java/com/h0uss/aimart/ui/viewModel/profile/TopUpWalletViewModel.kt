package com.h0uss.aimart.ui.viewModel.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.userRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class TopUpWalletViewModel : ViewModel(){

//    var state = MutableStateFlow(TopUpWalletState())
//    private set

    var navigationEvents = Channel<TopUpWalletNavigationEvent>()
    private set

    fun onEvent(event: TopUpWalletEvent) {
        when(event){
            is TopUpWalletEvent.TopUp -> {
                viewModelScope.launch {
                    userRepository.addBalance(authUserIdLong, 1000f)
                    navigationEvents.send(TopUpWalletNavigationEvent.Exit)
                }
            }
            is TopUpWalletEvent.Exit -> {
                viewModelScope.launch {
                    navigationEvents.send(TopUpWalletNavigationEvent.Exit)
                }
            }
        }
    }
}

//data class TopUpWalletState(
//    val user: UserData = UserData(),
//    val isVisibleSettings: Boolean = false,
//    val countBuy: Int = 0,
//)

sealed class TopUpWalletEvent {
    object TopUp : TopUpWalletEvent()
    object Exit : TopUpWalletEvent()
}

sealed class TopUpWalletNavigationEvent {
    object Exit: TopUpWalletNavigationEvent()
}
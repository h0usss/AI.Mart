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
class TopUpWalletViewModel : ViewModel() {

    var navigationEvents = Channel<TopUpWalletNavigationEvent>()
        private set

    fun onEvent(event: TopUpWalletEvent) {
        when (event) {
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

sealed class TopUpWalletEvent {
    object TopUp : TopUpWalletEvent()
    object Exit : TopUpWalletEvent()
}

sealed class TopUpWalletNavigationEvent {
    object Exit : TopUpWalletNavigationEvent()
}
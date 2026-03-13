package com.h0uss.aimart.ui.viewModel.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.data.model.ChatData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ChatsViewModel : ViewModel(){

    var state = MutableStateFlow(ChatsState())
        private set

    var navigationEvents = Channel<ChatsNavigationEvent>()
        private set

    fun onEvent(event: ChatsEvent) {
        when(event){
            is ChatsEvent.ChatClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ChatsNavigationEvent.Chat(event.value))
                }
            }
        }
    }
}

data class ChatsState(
    val chats: List<ChatData> = listOf(),
)

sealed class ChatsEvent {
    data class ChatClick(val value: Long) : ChatsEvent()
}

sealed class ChatsNavigationEvent {
    data class Chat(val value: Long) : ChatsNavigationEvent()
}

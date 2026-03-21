package com.h0uss.aimart.ui.viewModel.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.messageRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.data.model.MessageData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ChatUserViewModel(
    private val chatId: Long
): ViewModel() {

    var state = MutableStateFlow(ChatUserState())
        private set

    var navigationEvents = Channel<ChatUserNavigationEvent>()
        private set

    init {
        viewModelScope.launch {
            messageRepository.getMessagesByChatId(chatId).collect { messages ->
                state.update { it.copy(messages = messages) }
            }
        }

        viewModelScope.launch {
            userRepository.getOtherUserByChatId(chatId, authUserIdLong).collect { user ->
                state.update { it.copy(userData = user) }
            }
        }
    }

    fun onEvent(event: ChatUserEvent) {
        when(event){
            is ChatUserEvent.ToListClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ChatUserNavigationEvent.ChatList)
                }
            }
            is ChatUserEvent.UserClick -> {
                viewModelScope.launch {
                    val user = userRepository.getUserByIdFlow(event.value).first()
                    if (user.isSeller)
                        navigationEvents.send(ChatUserNavigationEvent.Seller(event.value))
                    else
                        navigationEvents.send(ChatUserNavigationEvent.User(event.value))
                }
            }
            is ChatUserEvent.SendMessage -> {
                viewModelScope.launch {
                    messageRepository.addMessageToChat(chatId, authUserIdLong, event.value)
                }
            }
        }
    }
}

data class ChatUserState(
    val messages: List<MessageData> = listOf(),
    val userData: ChatUserData = ChatUserData(),
)

sealed class ChatUserEvent {
    object ToListClick : ChatUserEvent()
    data class UserClick(val value: Long) : ChatUserEvent()
    data class SendMessage(val value: String) : ChatUserEvent()
}

sealed class ChatUserNavigationEvent {
    object ChatList : ChatUserNavigationEvent()
    data class Seller(val value: Long) : ChatUserNavigationEvent()
    data class User(val value: Long) : ChatUserNavigationEvent()
}

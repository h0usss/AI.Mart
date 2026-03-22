package com.h0uss.aimart.ui.viewModel.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.chatRepository
import com.h0uss.aimart.Graph.messageRepository
import com.h0uss.aimart.Graph.orderRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.data.model.UserData
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

        viewModelScope.launch {
            val chat = chatRepository.getChatById(chatId).first()

            if (chat != null && chat.orderId != null) {
                orderRepository.getOrderById(chat.orderId).collect { order ->
                    state.update { it.copy(orderData = order) }

                    launch {
                        userRepository.getUserByIdFlow(order.buyerId).collect { user ->
                            state.update { it.copy(buyer = user) }
                        }
                    }
                }
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
            is ChatUserEvent.ProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(ChatUserNavigationEvent.Product(event.value))
                }
            }
            is ChatUserEvent.SendMessage -> {
                viewModelScope.launch {
                    messageRepository.addMessageToChat(chatId, authUserIdLong, event.value)
                }
            }
            is ChatUserEvent.TaskBar -> {
                viewModelScope.launch {
                    navigationEvents.send(ChatUserNavigationEvent.TaskBar(state.value.orderData))
                }
            }
            is ChatUserEvent.PayClick -> {
                viewModelScope.launch {
                    val order = state.value.orderData
                    val buyer = state.value.buyer

                    if (buyer.balance < order.price) {
                        return@launch
                    }

                    val success = userRepository.transferFunds(
                        fromUserId = order.buyerId,
                        toUserId = order.sellerId,
                        amount = order.price
                    )

                    if (success) {
                        orderRepository.updateStatus(order.id, OrderStatus.COMPLETE)
                    }
                }
            }
        }
    }
}

data class ChatUserState(
    val messages: List<MessageData> = listOf(),
    val userData: ChatUserData = ChatUserData(),
    val buyer: UserData = UserData(),
    val orderData: OrderData = OrderData(),
)

sealed class ChatUserEvent {
    object ToListClick : ChatUserEvent()
    object PayClick : ChatUserEvent()
    object TaskBar : ChatUserEvent()
    data class UserClick(val value: Long) : ChatUserEvent()
    data class ProductClick(val value: Long) : ChatUserEvent()
    data class SendMessage(val value: String) : ChatUserEvent()
}

sealed class ChatUserNavigationEvent {
    object ChatList : ChatUserNavigationEvent()
    data class Seller(val value: Long) : ChatUserNavigationEvent()
    data class User(val value: Long) : ChatUserNavigationEvent()
    data class Product(val value: Long) : ChatUserNavigationEvent()
    data class TaskBar(val value: OrderData) : ChatUserNavigationEvent()
}

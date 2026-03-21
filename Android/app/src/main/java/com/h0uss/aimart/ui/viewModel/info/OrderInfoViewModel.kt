package com.h0uss.aimart.ui.viewModel.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.chatRepository
import com.h0uss.aimart.Graph.orderRepository
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.entity.ChatEntity
import com.h0uss.aimart.data.model.ChatData
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.data.model.ProductData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class OrderInfoViewModel(
    private val orderId: Long
) : ViewModel() {

    var state = MutableStateFlow(OrderInfoState())
        private set

    var navigationEvents = Channel<OrderInfoNavigationEvent>()
        private set

    init {
        orderRepository.getOrderById(orderId)
            .onEach { order ->
                state.update { it.copy(order = order) }

                val product = productRepository.getProductById(order.productId).firstOrNull()
                if (product != null) {
                    state.update { it.copy(product = product) }

                    val chats = chatRepository.getChatByOrderId(order.id, authUserIdLong).firstOrNull()
                    state.update { it.copy(chat = chats?.firstOrNull() ?: ChatData()) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: OrderInfoEvent) {
        when (event) {
            is OrderInfoEvent.Accept -> {
                viewModelScope.launch {
                    orderRepository.updateStatus(orderId, OrderStatus.IN_WORK)
                }
            }

            is OrderInfoEvent.Reject -> {
                viewModelScope.launch {
                    navigationEvents.send(OrderInfoNavigationEvent.ToOrderList)
                    orderRepository.updateStatus(orderId, OrderStatus.DELETED)
                }
            }

            is OrderInfoEvent.CreateChat -> {
                viewModelScope.launch {
                    val currentOrder = state.value.order

                    val existingChats = chatRepository.getChatByOrderId(currentOrder.id, authUserIdLong).firstOrNull()
                    var chat = existingChats?.firstOrNull()

                    if (chat == null) {
                        chatRepository.insert(
                            ChatEntity(
                                fUserId = authUserIdLong,
                                sUserId = currentOrder.buyerId,
                                orderId = currentOrder.id,
                                createdAt = LocalDateTime.now(),
                            )
                        )
                        val newChats = chatRepository.getChatByOrderId(currentOrder.id, authUserIdLong).firstOrNull()
                        chat = newChats?.firstOrNull()
                    }

                    chat?.let { foundChat ->
                        state.update { it.copy(chat = foundChat) }
                        navigationEvents.send(OrderInfoNavigationEvent.ToChat(foundChat.id))
                    }
                }
            }

            is OrderInfoEvent.CloseOrder -> {
                viewModelScope.launch {
                    orderRepository.updateStatus(orderId, OrderStatus.COMPLETE)
                }
            }

            is OrderInfoEvent.ToChat -> {
                viewModelScope.launch {
                    navigationEvents.send(OrderInfoNavigationEvent.ToChat(event.value))
                }
            }
        }
    }
}

data class OrderInfoState(
    val order: OrderData = OrderData(),
    val product: ProductData = ProductData(),
    val chat: ChatData = ChatData()
)

sealed class OrderInfoEvent {
    object Accept : OrderInfoEvent()
    object Reject : OrderInfoEvent()
    object CreateChat : OrderInfoEvent()
    object CloseOrder : OrderInfoEvent()
    data class ToChat(val value: Long) : OrderInfoEvent()
}

sealed class OrderInfoNavigationEvent {
    object ToOrderList : OrderInfoNavigationEvent()
    data class ToChat(val value: Long) : OrderInfoNavigationEvent()
}

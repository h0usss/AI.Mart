package com.h0uss.aimart.ui.viewModel.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.orderRepository
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.OrderCardData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class OrdersViewModel : ViewModel(){

    var state = MutableStateFlow(OrdersState())
        private set

    var navigationEvents = Channel<OrdersNavigationEvent>()
        private set

    init {
        orderRepository.getOrderByUserId(authUserIdLong)
            .onEach { newOrders ->
                val ordersByStatus = newOrders.groupBy { it.status }

                state.update { currentState ->
                    currentState.copy(
                        orders = newOrders,
                        countDebate     = ordersByStatus[OrderStatus.DEBATE]?.size ?: 0,
                        countWaiting    = ordersByStatus[OrderStatus.WAITING]?.size ?: 0,
                        countInWork     = ordersByStatus[OrderStatus.IN_WORK]?.size ?: 0,
                        countComplete   = ordersByStatus[OrderStatus.COMPLETE]?.size ?: 0
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: OrdersEvent) {
        when(event){
            is OrdersEvent.ProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(OrdersNavigationEvent.Product(event.value))
                }
            }
            is OrdersEvent.DebateClick -> {
                state.update{
                    it.copy(isDebate = !it.isDebate)
                }
            }
            is OrdersEvent.WaitingClick -> {
                state.update{
                    it.copy(isWaiting = !it.isWaiting)
                }
            }
            is OrdersEvent.InWorkClick -> {
                state.update{
                    it.copy(isInWork = !it.isInWork)
                }
            }
            is OrdersEvent.CompleteClick -> {
                state.update{
                    it.copy(isComplete = !it.isComplete)
                }
            }
        }
    }
}

data class OrdersState(
    val orders: List<OrderCardData> = listOf(),

    val isDebate: Boolean = false,
    val isWaiting: Boolean = false,
    val isInWork: Boolean = false,
    val isComplete: Boolean = false,

    val countDebate: Int = 0,
    val countWaiting: Int = 0,
    val countInWork: Int = 0,
    val countComplete: Int = 0,
)

sealed class OrdersEvent {
    data class ProductClick(val value: Long) : OrdersEvent()
    object DebateClick : OrdersEvent()
    object WaitingClick : OrdersEvent()
    object InWorkClick : OrdersEvent()
    object CompleteClick : OrdersEvent()
}

sealed class OrdersNavigationEvent {
    data class Product(val value: Long) : OrdersNavigationEvent()
}
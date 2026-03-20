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
        orderRepository.getOrdersByUserId(authUserIdLong)
            .onEach { newOrders ->
                val ordersByStatus = newOrders.groupBy { it.status }

                state.update { currentState ->
                    currentState.copy(
                        originalOrders = newOrders,
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
            is OrdersEvent.OrderClick -> {
                viewModelScope.launch {
                    navigationEvents.send(OrdersNavigationEvent.Order(event.value))
                }
            }
            is OrdersEvent.DebateClick -> {
                val wasActive = state.value.isDebate
                state.update {
                    it.copy(
                        isDebate = !wasActive,
                        isWaiting = false,
                        isInWork = false,
                        isComplete = false
                    )
                }
                applyFilters()
            }
            is OrdersEvent.WaitingClick -> {
                val wasActive = state.value.isWaiting
                state.update {
                    it.copy(
                        isDebate = false,
                        isWaiting = !wasActive,
                        isInWork = false,
                        isComplete = false
                    )
                }
                applyFilters()
            }
            is OrdersEvent.InWorkClick -> {
                val wasActive = state.value.isInWork
                state.update {
                    it.copy(
                        isDebate = false,
                        isWaiting = false,
                        isInWork = !wasActive,
                        isComplete = false
                    )
                }
                applyFilters()
            }
            is OrdersEvent.CompleteClick -> {
                val wasActive = state.value.isComplete
                state.update {
                    it.copy(
                        isDebate = false,
                        isWaiting = false,
                        isInWork = false,
                        isComplete = !wasActive
                    )
                }
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val currentState = state.value
        val activeFilters = buildSet {
            if (currentState.isDebate) add(OrderStatus.DEBATE)
            if (currentState.isWaiting) add(OrderStatus.WAITING)
            if (currentState.isInWork) add(OrderStatus.IN_WORK)
            if (currentState.isComplete) add(OrderStatus.COMPLETE)
        }

        val filteredList = if (activeFilters.isEmpty()) {
            currentState.originalOrders
        } else {
            currentState.originalOrders.filter { it.status in activeFilters }
        }

        state.update { it.copy(orders = filteredList) }
    }
}

data class OrdersState(
    val originalOrders: List<OrderCardData> = listOf(),
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
    data class OrderClick(val value: Long) : OrdersEvent()
    object DebateClick : OrdersEvent()
    object WaitingClick : OrdersEvent()
    object InWorkClick : OrdersEvent()
    object CompleteClick : OrdersEvent()
}

sealed class OrdersNavigationEvent {
    data class Order(val value: Long) : OrdersNavigationEvent()
}

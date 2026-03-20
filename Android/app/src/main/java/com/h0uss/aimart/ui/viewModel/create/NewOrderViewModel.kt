package com.h0uss.aimart.ui.viewModel.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.orderRepository
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.util.toLocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class NewOrderViewModel : ViewModel() {

    var state = MutableStateFlow(NewOrderState())
        private set

    var navigationEvents = Channel<NewOrderNavigationEvent>()
        private set

    fun onEvent(event: NewOrderEvent) {
        when (event) {
            is NewOrderEvent.Exit -> {
                viewModelScope.launch {
                    navigationEvents.send(NewOrderNavigationEvent.Exit)
                }
            }

            is NewOrderEvent.Send -> {
                viewModelScope.launch {
                    val currentState = state.value
                    orderRepository.insert(
                        OrderEntity(
                            price = currentState.price.text.toString().toFloatOrNull() ?: 0f,
                            status = OrderStatus.WAITING,
                            description = currentState.desc.text.toString(),
                            deadline = currentState.date.text.toString().toLocalDateTime(),
                            buyerId = authUserIdLong,
                            sellerId = event.sellerId,
                            productId = event.productId,
                            startDate = null,
                            completionDate = null,
                        )
                    )

                    navigationEvents.send(NewOrderNavigationEvent.Success)
                }
            }

            is NewOrderEvent.DateSelected -> {
                state.value.date.setTextAndPlaceCursorAtEnd(event.date)
            }
        }
    }
}

data class NewOrderState(
    val desc: TextFieldState = TextFieldState(""),
    val price: TextFieldState = TextFieldState(""),
    val date: TextFieldState = TextFieldState(""),
)

sealed class NewOrderEvent {
    object Exit : NewOrderEvent()
    data class Send(val sellerId: Long, val productId: Long) : NewOrderEvent()
    data class DateSelected(val date: String) : NewOrderEvent()
}

sealed class NewOrderNavigationEvent {
    object Exit : NewOrderNavigationEvent()
    object Success : NewOrderNavigationEvent()
}

package com.h0uss.aimart.ui.state.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.data.factory.OrderInfoViewModelFactory
import com.h0uss.aimart.ui.screen.info.OrderInfoScreen
import com.h0uss.aimart.ui.viewModel.info.OrderInfoNavigationEvent
import com.h0uss.aimart.ui.viewModel.info.OrderInfoViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderInfo(
    orderId: Long,
    navToBack: () -> Unit,
    navToChat: (Long) -> Unit,
) {
    val viewModel: OrderInfoViewModel = viewModel(
        factory = OrderInfoViewModelFactory(orderId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is OrderInfoNavigationEvent.ToOrderList -> {
                    navToBack()
                }
                is OrderInfoNavigationEvent.ToChat -> {
                    navToChat(event.value)
                }
            }
        }
    }

    OrderInfoScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navToBack
    )
}
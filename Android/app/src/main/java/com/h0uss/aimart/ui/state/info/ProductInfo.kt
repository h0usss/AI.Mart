package com.h0uss.aimart.ui.state.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.data.factory.ProductInfoViewModelFactory
import com.h0uss.aimart.ui.screen.info.ProductInfoScreen
import com.h0uss.aimart.ui.viewModel.info.ProductInfoNavigationEvent
import com.h0uss.aimart.ui.viewModel.info.ProductInfoViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductInfo(
    productId: Long,
    navToUser: (Long) -> Unit,
    onBuy: (Long, Long) -> Unit,
) {
    val viewModel: ProductInfoViewModel = viewModel(
        factory = ProductInfoViewModelFactory(productId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is ProductInfoNavigationEvent.User -> {
                    navToUser(event.value)
                }
            }
        }
    }

    ProductInfoScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBuy = onBuy
    )
}
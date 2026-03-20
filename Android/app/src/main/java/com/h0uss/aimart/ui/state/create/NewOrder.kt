package com.h0uss.aimart.ui.state.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.create.NewOrderScreen
import com.h0uss.aimart.ui.viewModel.create.NewOrderNavigationEvent
import com.h0uss.aimart.ui.viewModel.create.NewOrderViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewOrder(
    modifier: Modifier = Modifier,
    viewModel: NewOrderViewModel = viewModel<NewOrderViewModel>(),
    sellerId: Long,
    productId: Long,
    onExit: () -> Unit,
    onSuccess: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when (event) {
                is NewOrderNavigationEvent.Exit -> {
                    onExit()
                }

                is NewOrderNavigationEvent.Success -> {
                    onSuccess()
                }
            }
        }
    }

    NewOrderScreen(
        modifier = modifier,
        state = state,
        sellerId = sellerId,
        productId = productId,
        onEvent = viewModel::onEvent
    )
}
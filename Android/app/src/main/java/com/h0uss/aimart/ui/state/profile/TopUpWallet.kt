package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.profile.TopUpWalletScreen
import com.h0uss.aimart.ui.viewModel.profile.TopUpWalletNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.TopUpWalletViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopUpWallet(
    modifier: Modifier = Modifier,
    viewModel: TopUpWalletViewModel = viewModel<TopUpWalletViewModel>(),
    onExit: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is TopUpWalletNavigationEvent.Exit -> {
                    onExit()
                }
            }
        }
    }

    TopUpWalletScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
}
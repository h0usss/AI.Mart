package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.profile.SellerProfileForSelfScreen
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForSelf(
    viewModel: SellerProfileForSelfViewModel = viewModel<SellerProfileForSelfViewModel>(),
    navToPortfolioItem: (Long) -> Unit,
    navToEdit: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SellerProfileForSelfNavigationEvent.EditClick -> {
                    navToEdit()
                }
                is SellerProfileForSelfNavigationEvent.AddCaseClick -> {

                }
                is SellerProfileForSelfNavigationEvent.EmptiedAccountClick -> {

                }
                is SellerProfileForSelfNavigationEvent.ReplenishAccountClick -> {

                }
                is SellerProfileForSelfNavigationEvent.PortfolioItemClick -> {
                    navToPortfolioItem(event.id)
                }
            }
        }
    }

    SellerProfileForSelfScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
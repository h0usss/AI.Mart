package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.data.factory.SellerProfileForUserViewModelFactory
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.ui.screen.profile.SellerProfileForUserScreen
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForUserNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForUserViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForUser(
    userId: Long,
    navToBack: () -> Unit,
    navToChat: (Long) -> Unit,
    showPortfolio: (PortfolioItemData?) -> Unit,
) {

    val viewModel: SellerProfileForUserViewModel = viewModel(
        factory = SellerProfileForUserViewModelFactory(userId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SellerProfileForUserNavigationEvent.BackClick -> {
                    navToBack()
                }
                is SellerProfileForUserNavigationEvent.WriteClick -> {
                    navToChat(event.id)
                }
                is SellerProfileForUserNavigationEvent.ShowPortfolioItem -> {
                    showPortfolio(event.portfolioItem)
                }
            }
        }
    }

    SellerProfileForUserScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
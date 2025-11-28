package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.profile.SellerProfileForSelfEditScreen
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfEditNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfEditViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForSelfEdit(
    viewModel: SellerProfileForSelfEditViewModel = viewModel<SellerProfileForSelfEditViewModel>(),
    navToProfile: () -> Unit,
    navToCreateOrLogin: () -> Unit,
    navToPortfolioItem: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SellerProfileForSelfEditNavigationEvent.SaveClick -> {
                    navToProfile()
                }
                is SellerProfileForSelfEditNavigationEvent.ExitClick -> {
                    navToCreateOrLogin()
                }
                is SellerProfileForSelfEditNavigationEvent.DeleteAccountClick -> {
                    navToCreateOrLogin()
                }
                is SellerProfileForSelfEditNavigationEvent.AddCaseClick -> {

                }
                is SellerProfileForSelfEditNavigationEvent.PortfolioItemClick -> {
                    navToPortfolioItem(event.id)
                }
            }
        }
    }

    SellerProfileForSelfEditScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.profile.UserProfileForSelfScreen
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForSelfNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForSelfViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileForSelf(
    viewModel: UserProfileForSelfViewModel = viewModel<UserProfileForSelfViewModel>(),
    navToEditProfile: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is UserProfileForSelfNavigationEvent.EditClick -> {
                    navToEditProfile(event.id)
                }
                is UserProfileForSelfNavigationEvent.EmptiedAccount -> {
                }
                is UserProfileForSelfNavigationEvent.ReplenishAccount -> {
                }
            }
        }
    }

    UserProfileForSelfScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
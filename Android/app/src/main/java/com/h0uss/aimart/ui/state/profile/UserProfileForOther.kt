package com.h0uss.aimart.ui.state.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.data.factory.UserProfileForOtherViewModelFactory
import com.h0uss.aimart.ui.screen.profile.UserProfileForOtherScreen
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForOtherNavigationEvent
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForOtherViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileForOther(
    userId: Long,
    navToBack: () -> Unit,
) {
    val viewModel: UserProfileForOtherViewModel = viewModel(
        factory = UserProfileForOtherViewModelFactory(userId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is UserProfileForOtherNavigationEvent.BackClick -> {
                    navToBack()
                }
            }
        }
    }

    UserProfileForOtherScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
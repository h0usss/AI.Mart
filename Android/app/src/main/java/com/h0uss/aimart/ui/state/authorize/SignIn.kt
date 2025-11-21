package com.h0uss.aimart.ui.state.authorize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.authorize.SignInScreen
import com.h0uss.aimart.ui.viewModel.authorize.SignInNavigationEvent
import com.h0uss.aimart.ui.viewModel.authorize.SignInViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignIn(
    viewModel: SignInViewModel = viewModel<SignInViewModel>(),
    navToHome: () -> Unit,
    navToRegister: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SignInNavigationEvent.Success -> navToHome()
                is SignInNavigationEvent.NavigateToRegister -> navToRegister()
            }
        }
    }

    SignInScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

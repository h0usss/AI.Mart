package com.h0uss.aimart.ui.state.authorize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.authorize.SignUpScreen
import com.h0uss.aimart.ui.viewModel.authorize.SignUpNavigationEvent
import com.h0uss.aimart.ui.viewModel.authorize.SignUpViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUp(
    viewModel: SignUpViewModel = viewModel<SignUpViewModel>(),
    navToHome: () -> Unit,
    navToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SignUpNavigationEvent.Success -> navToHome()
                is SignUpNavigationEvent.NavigateToLogin -> navToLogin()
            }
        }
    }

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
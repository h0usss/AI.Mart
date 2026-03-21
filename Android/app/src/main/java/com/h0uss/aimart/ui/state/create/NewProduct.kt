package com.h0uss.aimart.ui.state.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.create.NewProductScreen
import com.h0uss.aimart.ui.viewModel.create.NewProductNavigationEvent
import com.h0uss.aimart.ui.viewModel.create.NewProductViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewProduct(
    modifier: Modifier = Modifier,
    viewModel: NewProductViewModel = viewModel<NewProductViewModel>(),
    onExit: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when (event) {
                is NewProductNavigationEvent.Exit -> {
                    onExit()
                }
            }
        }
    }

    NewProductScreen(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent
    )
}
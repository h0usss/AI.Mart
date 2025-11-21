package com.h0uss.aimart.ui.state.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.main.MyProductsScreen
import com.h0uss.aimart.ui.viewModel.main.MyProductsNavigationEvent
import com.h0uss.aimart.ui.viewModel.main.MyProductsViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyProducts(
    viewModel: MyProductsViewModel = viewModel<MyProductsViewModel>(),
    navToProduct: (Long) -> Unit,
    navToNewProduct: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is MyProductsNavigationEvent.Product -> {
                    navToProduct(event.value)
                }
                is MyProductsNavigationEvent.NewProduct -> {
                    navToNewProduct()
                }
            }
        }
    }

    MyProductsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
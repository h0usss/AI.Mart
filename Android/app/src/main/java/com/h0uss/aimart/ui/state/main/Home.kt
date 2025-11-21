package com.h0uss.aimart.ui.state.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.h0uss.aimart.ui.screen.main.HomeScreen
import com.h0uss.aimart.ui.viewModel.main.HomeNavigationEvent
import com.h0uss.aimart.ui.viewModel.main.HomeViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel<HomeViewModel>(),
    navToSeller: (Long) -> Unit,
    navToProduct: (Long) -> Unit,
    navToSearch: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is HomeNavigationEvent.Seller -> {
                    navToSeller(event.id)
                }
                is HomeNavigationEvent.Product -> {
                    navToProduct(event.id)
                }
                is HomeNavigationEvent.Search -> navToSearch()
            }
        }
    }

    HomeScreen(
        state = state,
        products = products,
        onEvent = viewModel::onEvent
    )
}
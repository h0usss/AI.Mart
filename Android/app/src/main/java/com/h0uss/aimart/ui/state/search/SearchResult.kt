package com.h0uss.aimart.ui.state.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.h0uss.aimart.ui.screen.search.SearchResultScreen
import com.h0uss.aimart.ui.viewModel.search.SearchNavigationEvent
import com.h0uss.aimart.ui.viewModel.search.SearchViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchResult(
    viewModel: SearchViewModel,
    navToSeller: (Long) -> Unit,
    navToProduct: (Long) -> Unit,
    navToSearchResult: () -> Unit,
    navToSearch: () -> Unit,
    navToHome: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is SearchNavigationEvent.Seller -> {
                    navToSeller(event.value)
                }
                is SearchNavigationEvent.Product -> {
                    navToProduct(event.value)
                }
                is SearchNavigationEvent.SearchEnter -> navToSearchResult()
                is SearchNavigationEvent.Back -> navToHome()
                is SearchNavigationEvent.SearchTextField -> navToSearch()
                is SearchNavigationEvent.CancelClick -> navToHome()
            }
        }
    }

    SearchResultScreen(
        products = products,
        state = state,
        onEvent = viewModel::onEvent
    )
}
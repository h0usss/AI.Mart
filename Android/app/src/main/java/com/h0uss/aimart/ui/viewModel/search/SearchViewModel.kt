package com.h0uss.aimart.ui.viewModel.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.Graph.searchHintRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.UserHomeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class SearchViewModel : ViewModel() {

    var state = MutableStateFlow(SearchState())
        private set

    var navigationEvents = Channel<SearchNavigationEvent>()
        private set

    private val searchTrigger = MutableStateFlow("")

    val products: Flow<PagingData<ProductCardData>> = searchTrigger
        .flatMapLatest { query ->
            if (query.isBlank())
                flowOf(PagingData.empty())
            else
                productRepository.getProductByStringInside(query)
        }
        .cachedIn(viewModelScope)

    init {
        searchHintRepository.getHints(authUserIdLong)
            .onEach { hintsFromDb ->
                val hintStrings = hintsFromDb.map { it.text }
                state.update { it.copy(lastSearchList = hintStrings) }
            }
            .launchIn(viewModelScope)

        searchTrigger
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList<UserHomeData>())
                } else {
                    userRepository.getUsersByStringInside(query)
                }
            }
            .onEach { sellers ->
                state.update { it.copy(sellers = sellers) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SearchEvent) {
        when(event){
            is SearchEvent.SellerClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.Seller(event.value))
                }
            }
            is SearchEvent.ProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.Product(event.value))
                }
            }
            is SearchEvent.HintClick -> {
                viewModelScope.launch {
                    searchHintRepository.updateTimeHint(
                        userId = authUserIdLong,
                        hintText = event.value
                    )
                    state.update {
                        it.copy(
                            searchState = TextFieldState(event.value)
                        )
                    }

                    searchTrigger.value = event.value

                    navigationEvents.send(SearchNavigationEvent.SearchEnter(
                        state.value.searchState.text.toString()
                    ))
                }
            }
            is SearchEvent.SearchClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchTextField)
                }
            }
            is SearchEvent.BackClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.Back)
                }
            }
            is SearchEvent.DeleteSearchClick -> {
                state.update {
                    it.copy(searchState = TextFieldState(""))
                }
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchTextField)
                }
            }
            is SearchEvent.DeleteHintClick -> {
                viewModelScope.launch {
                    searchHintRepository.deleteHint(
                        userId = authUserIdLong,
                        hintText = event.value
                    )
                }
            }
            is SearchEvent.ClearHintsClick -> {
                viewModelScope.launch {
                    searchHintRepository.deleteAllHint(userId = authUserIdLong)
                }
            }
            is SearchEvent.ClearSearchClick -> {
                state.update {
                    it.copy(searchState = TextFieldState(""))
                }
            }
            is SearchEvent.CancelClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.CancelClick)
                }
            }
            is SearchEvent.SearchRequest -> {
                val query = event.value
                state.update {
                    it.copy(searchState = TextFieldState(query))
                }

                searchTrigger.value = query

                if (query.isNotBlank()) {
                    viewModelScope.launch {
                        searchHintRepository.addHintIfNotExist(
                            userId = authUserIdLong,
                            hintText = query
                        )
                    }
                }

                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchEnter(query))
                }
            }
        }
    }
}


data class SearchState(
    val sellers: List<UserHomeData> = listOf(),
    val hints: List<String> = listOf(),
    val lastSearchList: List<String> = listOf(),
    val searchState: TextFieldState = TextFieldState("")
)

sealed class SearchEvent {
    data class SellerClick(val value: Long) : SearchEvent()
    data class ProductClick(val value: Long) : SearchEvent()
    data class HintClick(val value: String) : SearchEvent()
    data class DeleteHintClick(val value: String) : SearchEvent()
    data class SearchRequest(val value: String) : SearchEvent()
    object SearchClick : SearchEvent()
    object DeleteSearchClick : SearchEvent()
    object BackClick : SearchEvent()
    object CancelClick : SearchEvent()
    object ClearHintsClick : SearchEvent()
    object ClearSearchClick : SearchEvent()
}

sealed class SearchNavigationEvent {
    data class Seller(val value: Long) : SearchNavigationEvent()
    data class Product(val value: Long) : SearchNavigationEvent()
    data class SearchEnter(val value: String): SearchNavigationEvent()
    object SearchTextField: SearchNavigationEvent()
    object Back: SearchNavigationEvent()
    object CancelClick: SearchNavigationEvent()
}

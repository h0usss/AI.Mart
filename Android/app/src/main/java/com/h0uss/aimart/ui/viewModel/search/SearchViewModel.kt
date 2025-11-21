package com.h0uss.aimart.ui.viewModel.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.Graph.searchHintRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.UserHomeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class SearchViewModel : ViewModel(){

    var state = MutableStateFlow(SearchState())
        private set

    var navigationEvents = Channel<SearchNavigationEvent>()
        private set

    private val searchTrigger = MutableStateFlow<String?>(null)

    init {
        searchHintRepository.getHints(authUserIdLong)
            .onEach { hintsFromDb ->
                val hintStrings = hintsFromDb.map { it.text }
                state.update { it.copy(lastSearchList = hintStrings) }
            }
            .launchIn(viewModelScope)

        searchTrigger
            .filter { it != null }
            .flatMapLatest { query ->
                if (query.isNullOrBlank()) {
                    flowOf(Pair(emptyList<UserHomeData>(), emptyList<ProductCardData>()))
                } else {
                    combine(
                        userRepository.getUsersByStringInside(query),
                        productRepository.getProductByStringInside(query)
                    ) { sellers, products ->
                        Pair(sellers, products)
                    }
                }
            }
            .onEach { (sellers, products) ->
                state.update { it.copy(sellers = sellers, products = products) }
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

                    state.update { it.copy(searchValue = event.value) }

                    navigationEvents.send(SearchNavigationEvent.SearchEnter(state.value.searchValue))
                }
            }
            is SearchEvent.SearchClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchTextField(state.value.searchValue))
                }
            }
            is SearchEvent.BackClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.Back)
                }
            }
            is SearchEvent.DeleteSearchClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchTextField(value = ""))
                }
            }
            is SearchEvent.SearchValueChange -> {
                state.update {
                    it.copy(searchValue = event.value)
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
                    it.copy(searchValue = "")
                }
            }
            is SearchEvent.CancelClick -> {
                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.CancelClick)
                }
            }
            is SearchEvent.SearchRequest -> {
                val query = event.value
                if (query.isBlank()) return

                state.update {
                    it.copy(searchValue = query)
                }

                viewModelScope.launch {
                    searchHintRepository.addHintIfNotExist(
                        userId = authUserIdLong,
                        hintText = query
                    )
                }

                searchTrigger.value = query

                viewModelScope.launch {
                    navigationEvents.send(SearchNavigationEvent.SearchEnter(query))
                }

            }
        }
    }
}


data class SearchState(
    val sellers: List<UserHomeData> = listOf(),
    val products: List<ProductCardData> = listOf(),
    val hints: List<String> = listOf(),
    val lastSearchList: List<String> = listOf(),
    val searchValue: String = ""
)

sealed class SearchEvent {
    data class SellerClick(val value: Long) : SearchEvent()
    data class ProductClick(val value: Long) : SearchEvent()
    data class HintClick(val value: String) : SearchEvent()
    data class SearchValueChange(val value: String) : SearchEvent()
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
    data class SearchTextField(val value: String): SearchNavigationEvent()
    object Back: SearchNavigationEvent()
    object CancelClick: SearchNavigationEvent()
}
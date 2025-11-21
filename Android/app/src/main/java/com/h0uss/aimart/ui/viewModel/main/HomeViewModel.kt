package com.h0uss.aimart.ui.viewModel.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.AddData
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.UserHomeData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel: ViewModel() {

    var state = MutableStateFlow(HomeState())
        private set

    var navigationEvents = Channel<HomeNavigationEvent>()
        private set

    val products: Flow<PagingData<ProductCardData>> = productRepository.getProductsPagingData()
        .cachedIn(viewModelScope)

    init {
        getSellers()
    }

    fun onEvent(event: HomeEvent) {
        when(event){
            is HomeEvent.SearchClick -> {
                viewModelScope.launch {
                    navigationEvents.send(HomeNavigationEvent.Search)
                }
            }
            is HomeEvent.SellerClick -> {
                viewModelScope.launch {
                    navigationEvents.send(HomeNavigationEvent.Seller(event.id))
                }
            }
            is HomeEvent.ProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(HomeNavigationEvent.Product(event.id))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSellers() {
        userRepository.getSellerLimit(5)
            .onEach { newList ->
                state.update { 
                    it.copy(sellers = newList) 
                }
            }
            .launchIn(viewModelScope)
    }
}

data class HomeState(
    val add: List<AddData> = List(5){ item -> AddData(image = R.drawable.add_0, url = "", name = "") },
    val sellers: List<UserHomeData> = emptyList(),
)

sealed class HomeEvent{
    object SearchClick: HomeEvent()
    data class SellerClick(val id: Long): HomeEvent()
    data class ProductClick(val id: Long): HomeEvent()
}

sealed class HomeNavigationEvent{
    object Search: HomeNavigationEvent()
    data class Seller(val id: Long): HomeNavigationEvent()
    data class Product(val id: Long): HomeNavigationEvent()
}

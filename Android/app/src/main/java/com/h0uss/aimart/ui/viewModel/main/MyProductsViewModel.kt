package com.h0uss.aimart.ui.viewModel.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.UserProductCardData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class MyProductsViewModel : ViewModel() {

    var state = MutableStateFlow(MyProductsState())
        private set

    var navigationEvents = Channel<MyProductsNavigationEvent>()
        private set

    init {
        viewModelScope.launch {
            productRepository.getProductsByUserId(authUserIdLong)
                .onEach { newProducts ->
                    state.value = state.value.copy(products = newProducts)
                }
                .launchIn(viewModelScope)

            state.value = state.value.copy(
                balance = userRepository.getBalance(authUserIdLong).toString()
            )
        }
    }

    fun onEvent(event: MyProductsEvent) {
        when (event) {
            is MyProductsEvent.ProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(MyProductsNavigationEvent.Product(event.value))
                }
            }
            is MyProductsEvent.NewProductClick -> {
                viewModelScope.launch {
                    navigationEvents.send(MyProductsNavigationEvent.NewProduct)
                }
            }
        }
    }
}

data class MyProductsState(
    val products: List<UserProductCardData> = listOf(),
    val balance: String = "0.00",
)

sealed class MyProductsEvent {
    data class ProductClick(val value: Long) : MyProductsEvent()
    object NewProductClick : MyProductsEvent()
}

sealed class MyProductsNavigationEvent {
    data class Product(val value: Long) : MyProductsNavigationEvent()
    object NewProduct : MyProductsNavigationEvent()
}

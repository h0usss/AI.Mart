package com.h0uss.aimart.ui.viewModel.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.productRepository
import com.h0uss.aimart.data.emun.FormField
import com.h0uss.aimart.data.emun.ProductStatus
import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.util.validate.validateProductDesc
import com.h0uss.aimart.util.validate.validateProductImages
import com.h0uss.aimart.util.validate.validateProductName
import com.h0uss.aimart.util.validate.validateProductPrice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class NewProductViewModel : ViewModel() {

    var state = MutableStateFlow(NewProductState())
        private set

    var navigationEvents = Channel<NewProductNavigationEvent>()
        private set

    fun onEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.AddImages -> {
                state.update { currentState ->
                    val newList = (currentState.images + event.images).take(5)
                    currentState.copy(images = newList, imageError = null)
                }
            }

            is NewProductEvent.AddProduct -> {
                val currentState = state.value

                val imageError =
                    currentState.images.validateProductImages().takeIf { it.isNotEmpty() }
                val priceError =
                    currentState.price.text.toString().validateProductPrice()
                        .takeIf { it.isNotEmpty() }
                val nameError =
                    currentState.name.text.toString().validateProductName()
                        .takeIf { it.isNotEmpty() }
                val descError =
                    currentState.desc.text.toString().validateProductDesc()
                        .takeIf { it.isNotEmpty() }

                val hasError =
                    listOfNotNull(imageError, priceError, nameError, descError).any()

                if (hasError) {
                    state.update {
                        it.copy(
                            imageError = imageError,
                            priceError = priceError,
                            nameError = nameError,
                            descError = descError
                        )
                    }
                } else {
                    viewModelScope.launch {
                        productRepository.insert(
                            ProductEntity(
                                name = state.value.name.text.toString(),
                                imagesUrl = state.value.images,
                                price = state.value.price.text.toString().toFloat(),
                                description = state.value.desc.text.toString(),
                                createDate = LocalDateTime.now(),
                                productStatus = ProductStatus.ACTIVE,
                                userId = authUserIdLong,
                            )
                        )
                        navigationEvents.send(NewProductNavigationEvent.Exit)
                    }
                }
            }

            is NewProductEvent.ClearError -> {
                state.update { currentState ->
                    when (event.field) {
                        FormField.NAME -> currentState.copy(nameError = null)
                        FormField.IMAGE -> currentState.copy(imageError = null)
                        FormField.PRICE -> currentState.copy(priceError = null)
                        FormField.DESCRIPTION -> currentState.copy(descError = null)
                        else -> currentState
                    }
                }
            }
            
            is NewProductEvent.RemoveImage -> {
                state.update { it.copy(images = it.images.filterIndexed { index, _ -> index != event.index }) }
            }
        }
    }
}

data class NewProductState(
    val images: List<String> = listOf(),
    val price: TextFieldState = TextFieldState(""),
    val name: TextFieldState = TextFieldState(""),
    val desc: TextFieldState = TextFieldState(""),

    val imageError: String? = null,
    val priceError: String? = null,
    val nameError: String? = null,
    val descError: String? = null,
)

sealed class NewProductEvent {
    data class AddImages(val images: List<String>) : NewProductEvent()
    data class RemoveImage(val index: Int) : NewProductEvent()
    object AddProduct : NewProductEvent()
    data class ClearError(val field: FormField) : NewProductEvent()
}

sealed class NewProductNavigationEvent {
    object Exit : NewProductNavigationEvent()
}

package com.h0uss.aimart.data.factory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.h0uss.aimart.ui.viewModel.info.ProductInfoViewModel

class ProductInfoViewModelFactory(
    private val productId: Long,
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)) {
            return ProductInfoViewModel(productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
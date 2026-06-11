package com.h0uss.aimart.data.factory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.h0uss.aimart.ui.viewModel.info.ProductSellerInfoViewModel

class ProductSellerInfoViewModelFactory(
    private val productId: Long,
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductSellerInfoViewModel::class.java)) {
            return ProductSellerInfoViewModel(productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

package com.h0uss.aimart.data.factory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForOtherViewModel

class UserProfileForOtherViewModelFactory(
    private val userId: Long,
) : androidx.lifecycle.ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileForOtherViewModel::class.java)) {
            return UserProfileForOtherViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

package com.h0uss.aimart.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.h0uss.aimart.Graph.authUserIdLong

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckLogin(
    modifier: Modifier = Modifier,
    navToHome: () -> Unit,
    navToLogin: () -> Unit,
) {
    if (authUserIdLong == -1L)
        navToLogin()
    else
        navToHome()
}
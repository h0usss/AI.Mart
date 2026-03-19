package com.h0uss.aimart.ui.state.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.ui.screen.chat.ChatsScreen
import com.h0uss.aimart.ui.viewModel.chat.ChatsNavigationEvent
import com.h0uss.aimart.ui.viewModel.chat.ChatsViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chats(
    viewModel: ChatsViewModel = viewModel<ChatsViewModel>(),
    navToChat: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is ChatsNavigationEvent.Chat -> {
                    navToChat(event.value)
                }
            }
        }
    }

    ChatsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
package com.h0uss.aimart.ui.state.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h0uss.aimart.data.factory.ChatUserViewModelFactory
import com.h0uss.aimart.ui.screen.chat.ChatUserScreen
import com.h0uss.aimart.ui.viewModel.chat.ChatUserNavigationEvent
import com.h0uss.aimart.ui.viewModel.chat.ChatUserViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatUser(
    chatId: Long,
    navToChatList: () -> Unit,
    navToUser: (Long) -> Unit,
    navToSeller: (Long) -> Unit,
) {
    val viewModel: ChatUserViewModel = viewModel(
        factory = ChatUserViewModelFactory(chatId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.receiveAsFlow().collect { event ->
            when(event) {
                is ChatUserNavigationEvent.ChatList -> {
                    navToChatList()
                }
                is ChatUserNavigationEvent.Seller -> {
                    navToSeller(event.value)
                }
                is ChatUserNavigationEvent.User -> {
                    navToUser(event.value)
                }
            }
        }
    }

    ChatUserScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}
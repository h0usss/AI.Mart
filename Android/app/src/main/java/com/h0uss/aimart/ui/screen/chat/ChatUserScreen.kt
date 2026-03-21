package com.h0uss.aimart.ui.screen.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.ui.assets.SendMessageField
import com.h0uss.aimart.ui.assets.chat.ChatUserTopBar
import com.h0uss.aimart.ui.assets.chat.MyMessage
import com.h0uss.aimart.ui.assets.chat.OtherMessage
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.viewModel.chat.ChatUserEvent
import com.h0uss.aimart.ui.viewModel.chat.ChatUserState
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatUserScreen(
    modifier: Modifier = Modifier,
    state: ChatUserState = ChatUserState(),
    onEvent: (ChatUserEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ChatUserTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 55.dp, bottom = 14.dp),
            userData = state.userData,
            onBackClick = {
                onEvent(ChatUserEvent.ToListClick)
            },
            onUserClick = {
                onEvent(ChatUserEvent.UserClick(it))
            }
        )

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                ,
                verticalArrangement = Arrangement.spacedBy(
                    space = 14.dp,
                    alignment = Alignment.Bottom
                ),
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                }
                items(state.messages) { message ->
                    if (message.userId == authUserIdLong) {
                        MyMessage(
                            messageData = message
                        )
                    } else {
                        OtherMessage(
                            messageData = message,
                            onUserClick = {id ->
                                onEvent(ChatUserEvent.UserClick(id))
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                }
            }

            SendMessageField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                ,
                onClickEnter = { message ->
                    onEvent(ChatUserEvent.SendMessage(message))
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_Full() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                id = -1L,
                imagesId = List(4) { R.drawable.background },
                userName = "Pipipupu"
            ),
            messages = List(20){
                MessageData(
                    text = "1 test my msg",
                    date = LocalDateTime.now(),
                    avatarId = R.drawable.base_avatar,
                    userId = authUserIdLong,
                )
            }
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, widthDp = 360, heightDp = 1000)
@Composable
private fun Preview_Full_V2() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                id = -1L,
                imagesId = List(4) { R.drawable.background },
                userName = "Pipipupu"
            ),
            messages = listOf(
                MessageData(
                    text = "test my msg asd as das dasd as d da sdas da sdas dasd ",
                    date = LocalDateTime.now(),
                    avatarId = R.drawable.base_avatar,
                    userId = authUserIdLong,
                ),
                MessageData(
                    text = "test otasdasdasd as das as das das das das das das dasher msg",
                    date = LocalDateTime.now(),
                    avatarId = R.drawable.avatar_0,
                    userId = 2,
                ),
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
private fun Preview_Empty() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                id = -1L,
                imagesId = List(4) { R.drawable.background },
                userName = "Pipipupu"
            ),
            messages = listOf()
        )
    )
}

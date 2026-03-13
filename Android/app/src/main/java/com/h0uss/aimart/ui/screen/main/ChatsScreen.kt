package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.ChatData
import com.h0uss.aimart.ui.assets.card.ChatRowCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.main.ChatsEvent
import com.h0uss.aimart.ui.viewModel.main.ChatsState

@Composable
fun ChatsScreen(
    modifier: Modifier = Modifier,
    state: ChatsState = ChatsState(),
    onEvent: (ChatsEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 14.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 63.dp),
                    text = "Сообщения",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black100,
                    textAlign = TextAlign.Center,
                )
            }

            val chats = state.chats
            items(count=chats.size) { i ->
                if (i != 0){
                    HorizontalDivider(
                        modifier=Modifier
                            .padding(top=14.dp)
                        ,
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ChatRowCard(
                            modifier = Modifier
                                .clickable{
                                    onEvent(ChatsEvent.ChatClick(value = chats[i].id))
                                },
                            chat=chats[i],
                        )
                    }
                }
            }

        }
        if (state.chats.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 100.dp),
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.no_order),
                    contentDescription = "No orders"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    text = "Здесь пока ничего нет",
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black100,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview_Full() {
    ChatsScreen(
        state = ChatsState(
            chats = List(2) { item ->
                ChatData(
                    id = 1L,
                    imageId = R.drawable.base_avatar,
                    productName = "3D Model",
                    userName = "Детка геймер",
                    price = 100f,
                )
            }
        )
    )
}


@Preview(showSystemUi = true, widthDp = 360, heightDp = 1000)
@Composable
private fun Preview_Full_V2() {
    ChatsScreen(
        state = ChatsState(
            chats = List(10) { item ->
                ChatData(
                    id = 1L,
                    imageId = R.drawable.base_avatar,
                    productName = "3D Model",
                    userName = "Детка геймер",
                    price = 100f,
                )
            }
        )
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview_Empty() {
    ChatsScreen(
    )
}

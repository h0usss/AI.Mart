package com.h0uss.aimart.ui.assets.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.ChatData
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun ChatRowCard(
    modifier: Modifier = Modifier,
    chat: ChatData,
    onClick: (Long) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .clickable {
                onClick(chat.id)
            }
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier.weight(.8f)
        ){
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10))
                ,
                model = chat.imagesUrl[0],
                contentDescription = chat.productName,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
            ){
                Text(
                    modifier = Modifier
                    ,
                    text = chat.userName,
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black100
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                    ,
                    text = chat.productName ?: "",
                    style = regularStyle,
                    fontSize = 16.sp,
                    color = Black50,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                    ,
                    text = if (chat.price != null) "$${chat.price}" else "",
                    style = regularStyle,
                    fontSize = 16.sp,
                    color = Black100
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview_v1() {
    ChatRowCard(
        chat = ChatData(
            id = 1L,
            userName = "Пипипу",
            price = 100.00f,
            imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
            productName = "3d Модель",
        )
    )
}

@Preview
@Composable
private fun Preview_V2() {
    ChatRowCard(
        chat = ChatData(
            id = 1L,
            userName = "Пипипу",
            imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
        )
    )
}
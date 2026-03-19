package com.h0uss.aimart.ui.assets.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun ChatUserTopBar(
    modifier: Modifier = Modifier,
    userData: ChatUserData,
    onBackClick: () -> Unit = {},
    onUserClick: (Long) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .clickable {
                    onBackClick()
                },
            painter = painterResource(R.drawable.back),
            contentDescription = "Back"
        )

        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth(),
        ){
            if (userData.productImageId != -1) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onUserClick(userData.id)
                        },
                    painter = painterResource(userData.productImageId),
                    contentDescription = "ToUser",
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
            }
            
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 2.dp)
                    .clickable {
                        onUserClick(userData.id)
                    },
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 2.dp),
                    text = userData.userName,
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black80
                )
                Text(
                    text = "Был(а) недавно",
                    style = semiboldStyle,
                    fontSize = 12.sp,
                    color = Black50
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChatUserTopBar(
        userData = ChatUserData(
            id = 1L,
            productImageId = R.drawable.background_0,
            userName = "Детка геймер",
        )
    )
}

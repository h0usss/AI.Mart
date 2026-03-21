package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtherMessage(
    modifier: Modifier = Modifier,
    messageData: MessageData,
    onUserClick: (Long) -> Unit = {},
) {
    val time = messageData.date.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
    ) {

        Row(
            modifier = modifier
                .background(White)
            ,
            verticalAlignment = Alignment.Bottom
        ) {

            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onUserClick(messageData.userId)
                    }
                    .background(Black5)
                    .border(
                        color = Black10,
                        width = 1.dp,
                        shape = CircleShape
                    ),
                model = messageData.avatarUrl,
                contentDescription = "ToUser",
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 6.dp,
                            bottomEnd = 6.dp,
                        )
                    )
                    .background(Black5)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 2.dp),
                    text = messageData.text,
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black80
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 2.dp)
                .fillMaxWidth(),
            text = time,
            style = regularStyle,
            fontSize = 12.sp,
            color = Black50,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v1() {
    OtherMessage(
        messageData = MessageData(
            text = "Hello! Copying to the cloud does not work for me, help",
            date = LocalDateTime.now(),
            avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}"
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v2() {
    OtherMessage(
        modifier = Modifier.width(250.dp),
        messageData = MessageData(
            text = "Hello! Copying to the cloud does not work for me, help",
            date = LocalDateTime.now(),
            avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}"
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v3() {
    OtherMessage(
        modifier = Modifier.width(250.dp),
        messageData = MessageData(
            text = "Hello",
            date = LocalDateTime.now(),
            avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}"
        )
    )
}
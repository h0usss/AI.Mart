package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    onImageClick: (String) -> Unit = {},
    onVideoClick: (String) -> Unit = {},
) {
    val time = messageData.date.format(DateTimeFormatter.ofPattern("HH:mm"))

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            if (messageData.avatarUrl.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onUserClick(messageData.userId) }
                        .background(Black5)
                        .border(1.dp, Black10, CircleShape),
                    model = messageData.avatarUrl,
                    contentDescription = "ToUser",
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomEnd = 12.dp))
                    .background(Black5)
                    .padding(8.dp)
            ) {
                if (messageData.attachments.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        messageData.attachments.chunked(2).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                rowItems.forEach { url ->
                                    if (url.contains("/video/")) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.Black)
                                                .clickable { onVideoClick(url) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "\u25B6",
                                                color = White,
                                                fontSize = 32.sp,
                                            )
                                        }
                                    } else {
                                        AsyncImage(
                                            model = url,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { onImageClick(url) },
                                            contentScale = ContentScale.Crop,
                                        )
                                    }
                                }
                                if (rowItems.size == 1 && messageData.attachments.size > 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                if (messageData.text.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = messageData.text,
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 40.dp, top = 2.dp),
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

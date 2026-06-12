package com.h0uss.aimart.ui.assets.chat

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UseKtx")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyMessage(
    modifier: Modifier = Modifier,
    messageData: MessageData,
    onImageClick: (String) -> Unit = {},
    onVideoClick: (String) -> Unit = {},
) {
    val time = messageData.date.format(DateTimeFormatter.ofPattern("HH:mm"))

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp))
                .background(Teal)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            if (messageData.attachments.isNotEmpty()) {
                val chunks = messageData.attachments.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    chunks.forEachIndexed { index, rowItems ->
                        val isLastSingle = index == chunks.lastIndex && rowItems.size == 1
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            rowItems.forEach { url ->
                                val itemModifier = if (isLastSingle) {
                                    Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                } else {
                                    Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                }
                                val protected = messageData.isProtected
                                if (url.contains("/video/")) {
                                    Box(
                                        modifier = itemModifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black)
                                            .clickable { onVideoClick(url) },
                                    ) {
                                        VideoThumbnail(
                                            uri = url.toUri(),
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.Black.copy(alpha = 0.5f)),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = "\u25B6",
                                                color = White,
                                                fontSize = 24.sp,
                                            )
                                        }
                                        if (protected) {
                                            Text(
                                                text = "AI.MART",
                                                color = White.copy(alpha = 0.4f),
                                                fontSize = 10.sp,
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(4.dp)
                                                    .background(
                                                        Color.Black.copy(alpha = 0.4f),
                                                        RoundedCornerShape(2.dp)
                                                    )
                                                    .padding(horizontal = 3.dp, vertical = 1.dp),
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = itemModifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onImageClick(url) },
                                    ) {
                                        AsyncImage(
                                            model = url,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                        if (protected) {
                                            Text(
                                                text = "AI.MART",
                                                color = White.copy(alpha = 0.4f),
                                                fontSize = 10.sp,
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(4.dp)
                                                    .background(
                                                        Color.Black.copy(alpha = 0.4f),
                                                        RoundedCornerShape(2.dp)
                                                    )
                                                    .padding(horizontal = 3.dp, vertical = 1.dp),
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowItems.size == 1 && !isLastSingle) {
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
                    color = White
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = time,
            style = regularStyle,
            fontSize = 12.sp,
            color = Black50,
            textAlign = TextAlign.End
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v1() {
    MyMessage(
        messageData = MessageData(
            text = "Hello! Copying to the cloud does not work for me, help",
            date = LocalDateTime.now()
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v2() {
    MyMessage(
        modifier = Modifier.width(250.dp),
        messageData = MessageData(
            text = "Hello! Copying to the cloud does not work for me, help",
            date = LocalDateTime.now()
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v3() {
    MyMessage(
        modifier = Modifier.width(250.dp),
        messageData = MessageData(
            text = "Hello! ",
            date = LocalDateTime.now()
        )
    )
}

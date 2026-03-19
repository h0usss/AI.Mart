package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyMessage(
    modifier: Modifier = Modifier,
    messageData: MessageData,
) {
    val time = messageData.date.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp,
                    bottomStart = 6.dp,
                ))
                .background(Teal)
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
        ){
            Text(
                modifier = Modifier
                    .padding(bottom = 2.dp),
                text = messageData.text,
                style = regularStyle,
                fontSize = 14.sp,
                color = White
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth()
            ,
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
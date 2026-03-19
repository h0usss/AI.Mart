package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.TealCounterBg
import com.h0uss.aimart.ui.theme.TealCounterBorder
import com.h0uss.aimart.ui.theme.TealCounterText
import com.h0uss.aimart.ui.theme.mediumStyle

@Composable
fun NotificationsCount(
    modifier: Modifier = Modifier,
    count: Int,
    isActive: Boolean = false
) {
    Box(
        modifier = modifier
            .height(16.dp)
            .defaultMinSize(minWidth = 16.dp)
            .clip(CircleShape)
            .background(if (isActive) TealCounterBg else Black5)
            .border(
                width = 1.dp,
                color = if (isActive) TealCounterBorder else Black10,
                shape = CircleShape
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier.padding(top = 1.dp, bottom = 2.dp, start = 3.dp, end = 3.dp),
            text = count.toString(),
            color = if (isActive) TealCounterText else Black80,
            fontSize = 12.sp,
            style = mediumStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Column {
        Row() {
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 9
            )
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 99
            )
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 999
            )
            NotificationsCount(
                count = 9999
            )
        }
        Row() {
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 9,
                isActive = true
            )
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 99,
                isActive = true
            )
            NotificationsCount(
                modifier = Modifier.padding(end = 5.dp),
                count = 999,
                isActive = true
            )
            NotificationsCount(
                count = 9999,
                isActive = true
            )
        }
    }
}
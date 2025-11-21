package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.StatusData
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Complete
import com.h0uss.aimart.ui.theme.Debate
import com.h0uss.aimart.ui.theme.InWork
import com.h0uss.aimart.ui.theme.TealTagBg
import com.h0uss.aimart.ui.theme.TealTagBorder
import com.h0uss.aimart.ui.theme.Waiting
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun Status(
    modifier: Modifier = Modifier,
    statusData: StatusData,
) {
    var name: String
    var color: Color

    when(statusData.status){
        OrderStatus.WAITING -> {
            name = "Ожидание"
            color = Waiting
        }
        OrderStatus.IN_WORK -> {
            name = "В работе"
            color = InWork
        }
        OrderStatus.DEBATE -> {
            name = "Спор"
            color = Debate
        }
        OrderStatus.COMPLETE -> {
            name = if (statusData.isTag) "Завершенные" else "Завершен"
            color = Complete
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (statusData.isActive) TealTagBg else White)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(50),
                color = if (statusData.isActive) TealTagBorder else Black10,
            )
            .padding(
                horizontal = if (statusData.isTag) 12.dp else 8.dp,
                vertical = if (statusData.isTag) 6.dp else 2.dp
            )
            .clickable{
                statusData.onClick()
            }
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
            ,
            text = name,
            style = regularStyle,
            fontSize = 14.sp,
            color = Black80
        )
        if (statusData.count > 0)
            Text(
                text = " (${statusData.count})",
                style = regularStyle,
                fontSize = 14.sp,
                color = Black80
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Column{
        Row {
            listOf(true, false).forEach { isTag ->
                Column {
                    OrderStatus.entries.forEach { item ->
                        Status(
                            modifier = Modifier.padding(bottom = 5.dp),
                            statusData = StatusData(
                                status = item,
                                count = 0,
                                isTag = isTag,
                            )
                        )
                    }
                }
            }
            Column {
                OrderStatus.entries.forEach { item ->
                    Status(
                        modifier = Modifier.padding(bottom = 5.dp),
                        statusData = StatusData(
                            status = item,
                            isTag = true,
                            isActive = true
                        ),
                    )
                }
            }
        }
        Row {
            Column {
                OrderStatus.entries.forEach { item ->
                    Status(
                        modifier = Modifier.padding(bottom = 5.dp),
                        statusData = StatusData(
                            status = item,
                            isTag = true,
                            count = 100,
                            isActive = true
                        ),
                    )
                }
            }
            Column {
                OrderStatus.entries.forEach { item ->
                    Status(
                        modifier = Modifier.padding(bottom = 5.dp),
                        statusData = StatusData(
                            status = item,
                            isTag = true,
                            count = 100
                        )
                    )
                }
            }
        }
    }
}
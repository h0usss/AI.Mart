package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MiniTaskBar(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(8.dp)
            )
            .background(White)
            .clickable {
                onClick()
            }
            .padding(8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(end = 16.dp)
            ,
            painter = painterResource(R.drawable.unpin),
            contentDescription = "Unpin"
        )

        Column(
            modifier = Modifier
                .background(White)
            ,
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 2.dp),
                text = "Задача",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black80
            )
            if (orderData.description != null)
                Text(
                    text = orderData.description,
                    style = regularStyle,
                    fontSize = 12.sp,
                    color = Black80,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v1() {
    MiniTaskBar(
        orderData = OrderData(
            description = "asdasdas das as asdasd "
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v2() {
    MiniTaskBar(
        orderData = OrderData(
            description = LoremIpsum(70).values.joinToString(" ") { it }
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v3() {
    MiniTaskBar(
        orderData = OrderData()
    )
}
package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.util.customFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskBar(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onExit: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable { onExit() }
            .padding(start = 16.dp, top = 111.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(24.dp)
                .clickable(enabled = false) {},
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Задача",
                style = semiboldStyle,
                color = Black80,
                fontSize = 16.sp,
            )
            if (orderData.description != null)
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = orderData.description,
                    style = regularStyle,
                    color = Black80,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify
                )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = "Срок",
                    style = semiboldStyle,
                    color = Black80,
                    fontSize = 16.sp,
                )
                Text(
                    text = if (orderData.deadline != null) "до ${orderData.deadline.format(customFormatter)}" else "",
                    style = regularStyle,
                    color = Black80,
                    fontSize = 14.sp,
                )
            }

        }

        Image(
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable { onExit() }
            ,
            painter = painterResource(R.drawable.close_portfolio),
            contentDescription = "Close order"
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v1() {
    TaskBar(
        orderData = OrderData(
            description = "asdasdas das as asdasd "
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v2() {
    TaskBar(
        orderData = OrderData(
            description = LoremIpsum(100).values.joinToString(" ") { it }
        )
    )
}
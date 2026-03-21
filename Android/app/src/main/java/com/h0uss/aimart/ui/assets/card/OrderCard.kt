package com.h0uss.aimart.ui.assets.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.OrderCardData
import com.h0uss.aimart.data.model.StatusData
import com.h0uss.aimart.ui.assets.Status
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun OrderCard(
    modifier: Modifier = Modifier,
    order: OrderCardData,
    onClick: (Long) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(White)
            .clickable {
                onClick(order.id)
            }
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopEnd
        ){
            AsyncImage(
                modifier = Modifier
                    .size(164.dp)
                    .clip(RoundedCornerShape(5))
                ,
                model = order.imagesUrl[0],
                contentDescription = "Product image",
                contentScale = ContentScale.Crop
            )

            Status(
                modifier = Modifier.padding(top = 6.dp, end = 3.dp),
                statusData = StatusData(
                    status = order.status,
                    isTag = false
                )
            )
        }
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = order.name,
            style = regularStyle,
            fontSize = 14.sp,
            color = Black100
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = "$${order.price}",
            style = semiboldStyle,
            fontSize = 16.sp,
            color = Black100
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Row{
        OrderCard(
            modifier = Modifier.padding(end = 5.dp),
            order = OrderCardData(
                id = 1L,
                name = "ProductName",
                price = 10.99f,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                status = OrderStatus.COMPLETE
            )
        )
        OrderCard(
            order = OrderCardData(
                id = 1L,
                name = "ProductName",
                price = 10.99f,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                status = OrderStatus.COMPLETE
            )
        )
    }
}
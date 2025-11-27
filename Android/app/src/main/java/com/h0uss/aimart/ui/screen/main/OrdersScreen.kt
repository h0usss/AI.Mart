package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.OrderCardData
import com.h0uss.aimart.data.model.StatusData
import com.h0uss.aimart.ui.assets.Status
import com.h0uss.aimart.ui.assets.card.OrderCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.main.OrdersEvent
import com.h0uss.aimart.ui.viewModel.main.OrdersState

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    state: OrdersState = OrdersState(),
    onEvent: (OrdersEvent) -> Unit = {},
) {
    val tags = listOf(
        StatusData(
            status = OrderStatus.DEBATE,
            count = state.countDebate,
            isTag = true,
            isActive = state.isDebate,
            onClick = {
                onEvent(OrdersEvent.DebateClick)
            }
        ),
        StatusData(
            status = OrderStatus.WAITING,
            count = state.countWaiting,
            isTag = true,
            isActive = state.isWaiting,
            onClick = {
                onEvent(OrdersEvent.WaitingClick)
            }
        ),
        StatusData(
            status = OrderStatus.IN_WORK,
            count = state.countInWork,
            isTag = true,
            isActive = state.isInWork,
            onClick = {
                onEvent(OrdersEvent.InWorkClick)
            }
        ),
        StatusData(
            status = OrderStatus.COMPLETE,
            count = state.countComplete,
            isTag = true,
            isActive = state.isComplete,
            onClick = {
                onEvent(OrdersEvent.CompleteClick)
            }
        ),
    )

    Column(
        modifier = modifier
            .background(White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ){
            item{
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 63.dp)
                    ,
                    text = "Заказы",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black100,
                    textAlign = TextAlign.Center,
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    tags.forEach { item ->
                        Status(
                            statusData = item,
                        )
                    }
                }
            }

            val chunkedProducts = state.orders.chunked(2)

            items(chunkedProducts) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { order ->
                        OrderCard(
                            order = order
                        )
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            if (state.orders.isNotEmpty())
                item {
                    Spacer(modifier = Modifier.height(85.dp))
                }
        }
        if (state.orders.isEmpty()){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 100.dp)
                ,
            ){
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.no_order),
                    contentDescription = "No orders"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                    ,
                    text = "Здесь пока ничего нет",
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black100,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview_Full() {
    OrdersScreen(
        state = OrdersState(
            orders = List(21) { item ->
                OrderCardData(
                    id = 1L,
                    name = "AI Кольцо всевластия",
                    price = 0.99f,
                    imageId = R.drawable.background,
                    status = OrderStatus.COMPLETE,
                )
            },
        )

    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview_Empty() {
    OrdersScreen(
        state = OrdersState(
            orders = listOf()
        ),
    )
}

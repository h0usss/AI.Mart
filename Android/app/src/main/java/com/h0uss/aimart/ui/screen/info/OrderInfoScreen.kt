package com.h0uss.aimart.ui.screen.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.emun.ProductStatus
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.data.model.ProductData
import com.h0uss.aimart.data.model.StatusData
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Status
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.info.OrderInfoEvent
import com.h0uss.aimart.ui.viewModel.info.OrderInfoState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderInfoScreen(
    modifier: Modifier = Modifier,
    state: OrderInfoState = OrderInfoState(),
    onEvent: (OrderInfoEvent) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 63.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clickable {
                        onBack()
                    },
                painter = painterResource(R.drawable.back),
                contentDescription = "Back",
            )
            Text(
                text = "Заказ № ${state.order.id}",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black100
            )
            Status(
                statusData = StatusData(
                    status = state.order.status,
                    isTag = false
                )
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues()
        ) {
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.product.imagesId) { imageId ->
                        Image(
                            modifier = Modifier
                                .size(240.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = painterResource(imageId),
                            contentDescription = "Product image $imageId",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 18.dp),
                        text = "Описание",
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    Text(
                        text = state.order.description.toString(),
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80,
                        textAlign = TextAlign.Justify
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Срок",
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black80,
                )
                Text(
                    text = "до ${state.order.deadline?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black80,
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Бюджет",
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black80,
                )
                Text(
                    text = "${state.order.price}$",
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black80,
                )
            }
            when (state.order.status) {
                OrderStatus.WAITING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f)
                                    .fillMaxWidth(),
                                isGreen = true,
                                text="Принять",
                                onClick = {
                                    onEvent(OrderInfoEvent.Accept)
                                }
                            )
                            Button(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                                    .fillMaxWidth(),
                                isRedFill = true,
                                text="Отклонить",
                                onClick = {
                                    onEvent(OrderInfoEvent.Reject)
                                }
                            )
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            isGray = true,
                            text="Написать заказчику",
                            onClick = {
                                onEvent(OrderInfoEvent.CreateChat)
                            }
                        )
                    }
                }
                OrderStatus.IN_WORK -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            isGreen = true,
                            text="Завершить заказ",
                            onClick = {
                                onEvent(OrderInfoEvent.CloseOrder)
                            }
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            isGray = true,
                            text="Чат с заказчиком",
                            onClick = {
                                onEvent(OrderInfoEvent.ToChat(state.chat.id))
                            }
                        )
                    }
                }
                OrderStatus.COMPLETE, OrderStatus.DEBATE -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        isGray = true,
                        text="Чат с заказчиком",
                        onClick = {
                            onEvent(OrderInfoEvent.ToChat(state.order.buyerId))
                        }
                    )
                }
                OrderStatus.DELETED -> {}
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 1000)
@Composable
private fun Preview_v1() {
    OrderInfoScreen(
        state = OrderInfoState(
            product = ProductData(
                author = "popo",
                name = "productname",
                price = 10.1f,
                imagesId = List(4) { R.drawable.background },
                status = ProductStatus.ACTIVE,
            ),
            order = OrderData(
                price = 20.1f,
                status = OrderStatus.WAITING,
                description = LoremIpsum(50).values.joinToString(" ") { it },
                deadline = LocalDateTime.now(),
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 1000)
@Composable
private fun Preview_v2() {
    OrderInfoScreen(
        state = OrderInfoState(
            product = ProductData(
                author = "popo",
                name = "productname",
                price = 10.1f,
                imagesId = List(4) { R.drawable.background },
                status = ProductStatus.ACTIVE,
            ),
            order = OrderData(
                price = 20.1f,
                status = OrderStatus.COMPLETE,
                description = LoremIpsum(500).values.joinToString(" ") { it },
                deadline = LocalDateTime.now(),
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 1000)
@Composable
private fun Preview_v3() {
    OrderInfoScreen(
        state = OrderInfoState(
            product = ProductData(
                author = "popo",
                name = "productname",
                price = 10.1f,
                imagesId = List(4) { R.drawable.background },
                status = ProductStatus.ACTIVE,
            ),
            order = OrderData(
                price = 20.1f,
                status = OrderStatus.IN_WORK,
                description = LoremIpsum(50).values.joinToString(" ") { it },
                deadline = LocalDateTime.now(),
            )
        )
    )
}
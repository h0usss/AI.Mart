package com.h0uss.aimart.ui.screen.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.enum.ProductStatus
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.ProductData
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Feedback
import com.h0uss.aimart.ui.assets.SellerHint
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black45
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.info.ProductSellerInfoEvent
import com.h0uss.aimart.ui.viewModel.info.ProductSellerInfoState
import com.h0uss.aimart.util.formatPrice
import java.time.LocalDateTime
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductSellerInfoScreen(
    modifier: Modifier = Modifier,
    state: ProductSellerInfoState = ProductSellerInfoState(),
    onEvent: (ProductSellerInfoEvent) -> Unit = {},
    onBuy: (Long, Long) -> Unit = { sellerId, productId -> },
    onBackClick: () -> Unit = {},
) {
    var countFeedbackItem by remember { mutableIntStateOf(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 63.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    },
                painter = painterResource(R.drawable.back),
                contentDescription = "Back",
            )
            Row() {
                Image(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.share_32),
                    contentDescription = "Share",
                )
                Image(
                    modifier = Modifier
                        .clickable {
                            onEvent(ProductSellerInfoEvent.EditClick)
                        },
                    painter = painterResource(R.drawable.edit),
                    contentDescription = "Edit",
                )
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues()
        ) {
            item {
                Column {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.product.imagesUrl) { imageId ->
                            AsyncImage(
                                modifier = Modifier
                                    .size(240.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                model = imageId,
                                contentDescription = "Product image $imageId",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 14.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Image(
                                painter = painterResource(R.drawable.eye),
                                contentDescription = "Просмотры",
                                modifier = Modifier.padding(end = 10.dp),
                            )
                            val displayViews = if (state.todayViews > 0) {
                                "${state.totalViews - state.todayViews} +${state.todayViews}"
                            } else {
                                state.totalViews.toString()
                            }
                            Text(
                                text = displayViews,
                                style = regularStyle,
                                fontSize = 14.sp,
                                color = Black45,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Image(
                                painter = painterResource(R.drawable.cart),
                                contentDescription = "Покупки",
                                modifier = Modifier.padding(end = 10.dp),
                            )
                            val displayBuy = "0"
                            Text(
                                text = displayBuy,
                                style = regularStyle,
                                fontSize = 14.sp,
                                color = Black45,
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "от ${state.product.price.formatPrice()}₽",
                        style = semiboldStyle,
                        fontSize = 24.sp,
                        color = Black100
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 18.dp),
                        text = state.product.name,
                        style = regularStyle,
                        fontSize = 16.sp,
                        color = Black80
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 18.dp),
                        text = "Описание",
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    Text(
                        modifier = Modifier.height(100.dp),
                        text = state.product.description,
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80,
                        textAlign = TextAlign.Justify
                    )
                }
            }
            if (state.product.status == ProductStatus.ACTIVE || state.product.status == ProductStatus.ARCHIVE) {
                item {
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    ) {
                        Text(
                            text = "Отзывы",
                            style = semiboldStyle,
                            fontSize = 18.sp,
                            color = Black80
                        )
                        FlowRow(
                            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SellerHint(
                                modifier = Modifier.clickable {
                                    onEvent(ProductSellerInfoEvent.FeedbackTagClick(0))
                                },
                                text = "Все",
                                isActive = state.feedbackFilter[0]
                            )
                            SellerHint(
                                modifier = Modifier.clickable {
                                    onEvent(ProductSellerInfoEvent.FeedbackTagClick(1))
                                },
                                text = "Высокий рейтинг",
                                leftImageId = R.drawable.high_rating,
                                isActive = state.feedbackFilter[1]
                            )
                            SellerHint(
                                modifier = Modifier.clickable {
                                    onEvent(ProductSellerInfoEvent.FeedbackTagClick(2))
                                },
                                text = "Низкий рейтинг",
                                leftImageId = R.drawable.low_rating,
                                isActive = state.feedbackFilter[2]
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (i in 0..min(countFeedbackItem - 1, state.filteredFeedback.size - 1))
                                Feedback(
                                    feedbackData = state.filteredFeedback[i]
                                )
                        }
                        if (state.filteredFeedback.size > countFeedbackItem)
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                text = "Показать еще",
                                isGray = true,
                                onClick = {
                                    countFeedbackItem += 3
                                }
                            )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 2000)
@Composable
private fun Preview() {
    ProductSellerInfoScreen(
        state = ProductSellerInfoState(
            product = ProductData(
                author = "popo",
                name = "productname",
                price = 10.1f,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                status = ProductStatus.ACTIVE,
            ),
            filteredFeedback = List(10) { item ->
                FeedbackData(
                    user = UserData(
                        name = "Гена",
                        nick = "@df",
                        imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                        rate = 5.0f,
                    ),
                    text = "Отзыв отзыв",
                    starCount = 5,
                    date = LocalDateTime.of(2022, 12, 11, 11, 11)
                )
            },
        )
    )
}

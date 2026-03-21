package com.h0uss.aimart.ui.screen.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.data.model.SellerData
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Dropdown
import com.h0uss.aimart.ui.assets.Feedback
import com.h0uss.aimart.ui.assets.Hint
import com.h0uss.aimart.ui.assets.SellerHint
import com.h0uss.aimart.ui.assets.StatisticBox
import com.h0uss.aimart.ui.assets.TopBar
import com.h0uss.aimart.ui.assets.card.PortfolioCard
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Black90
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForUserEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForUserState
import java.time.LocalDateTime
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForUserScreen(
    modifier: Modifier = Modifier,
    state: SellerProfileForUserState = SellerProfileForUserState(),
    onEvent: (SellerProfileForUserEvent) -> Unit = {},
) {
    val chunkedProducts = state.portfolio.chunked(2)
    var countPortfolioRowItem by remember{ mutableIntStateOf(2) }
    var countFeedbackItem by remember{ mutableIntStateOf(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
        ,
    ){
        Dropdown(
            isAdditional = true,
            isVisible = state.isShowAdditional,
            offset = DpOffset(
                x = (-40).dp,
                y = (0).dp
            ),
            onItemClick = { id ->
                onEvent(SellerProfileForUserEvent.AdditionalItemClick(id))
            },
            onDismissRequest = {
                onEvent(SellerProfileForUserEvent.DismissAdditionalMenu)
            }
        )
        TopBar(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
            onBackClick = {
                onEvent(SellerProfileForUserEvent.BackClick)
            },
            onAdditionalClick = {
                onEvent(SellerProfileForUserEvent.ShowAdditionalMenu)
            },
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(White)
                            .border(
                                width = 1.dp,
                                color = Black10,
                                shape = RoundedCornerShape(15.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(96.dp)
                                .clip(CircleShape),
                            painter = painterResource(state.user.imageId),
                            contentDescription = state.user.nick,
                        )
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = state.user.name,
                            style = semiboldStyle,
                            fontSize = 18.sp,
                            color = Black80
                        )
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
                            text = state.user.nick,
                            style = regularStyle,
                            fontSize = 16.sp,
                            color = Black50
                        )
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = state.user.profession,
                            style = regularStyle,
                            fontSize = 16.sp,
                            color = Black90
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                            ,
                            text = "Написать продавцу",
                            onClick = {
                                onEvent(SellerProfileForUserEvent.WriteClick)
                            },
                        )
                    }
                    if (state.isLike)
                        Image(
                            modifier = Modifier
                                .padding(top = 12.dp, end = 14.dp)
                                .clickable {
                                    onEvent(SellerProfileForUserEvent.LikeClick)
                                },
                            painter = painterResource(R.drawable.like_red),
                            contentDescription = "Like red"
                        )
                    else
                        Image(
                            modifier = Modifier
                                .padding(top = 12.dp, end = 14.dp)
                                .clickable {
                                    onEvent(SellerProfileForUserEvent.LikeClick)
                                },
                            painter = painterResource(R.drawable.like),
                            contentDescription = "Like no red"
                        )
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatisticBox(
                        modifier = Modifier.weight(.3f),
                        imageId = R.drawable.star,
                        num = state.user.rate.toString(),
                        name = "Рейтинг"
                    )
                    StatisticBox(
                        modifier = Modifier.weight(.3f),
                        imageId = R.drawable.feedback,
                        num = state.originalFeedback.size.toString(),
                        name = "Отзывы"
                    )
                    StatisticBox(
                        modifier = Modifier.weight(.3f),
                        imageId = R.drawable.handshake,
                        num = state.countSell.toString(),
                        name = "Сделки"
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            color = Black10,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "О себе",
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = state.user.about,
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                        text = "Навыки",
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.user.skills.forEach { skill ->
                            Hint( text = skill )
                        }
                    }
                }
            }
            item{
                Column{
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "Портфолио",
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )

                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        itemsIndexed(state.allPortfolioTags){ index, item ->
                            SellerHint(
                                modifier = Modifier.clickable{
                                    onEvent(SellerProfileForUserEvent.PortfolioTagClick(item))
                                },
                                text = item,
                                isActive = state.portfolioTagFilter[index]
                            )
                        }
                    }

                    for (i in 0..min(countPortfolioRowItem - 1, chunkedProducts.size - 1)){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, top = 16.dp, end = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            chunkedProducts[i].forEach { portfolio ->
                                PortfolioCard(
                                    modifier = Modifier.clickable{
                                        onEvent(
                                            SellerProfileForUserEvent.ShowPortfolioItem(
                                                portfolio.id
                                            )
                                        )
                                    },
                                    portfolioData = portfolio,
                                    isExistTrash = false
                                )
                            }
                            if (chunkedProducts[i].size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    if (chunkedProducts.size > countPortfolioRowItem)
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, top = 16.dp, end = 32.dp)
                            ,
                            text = "Показать еще",
                            isGray = true,
                            onClick = {
                                countPortfolioRowItem += 2
                            }
                        )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(start = 32.dp, top = 16.dp, end = 32.dp),
                ){
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
                    ){
                        SellerHint(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForUserEvent.FeedbackTagClick(0))
                            },
                            text = "Все",
                            isActive = state.feedbackTagFilter[0]
                        )
                        SellerHint(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForUserEvent.FeedbackTagClick(1))
                            },
                            text = "Высокий рейтинг",
                            leftImageId = R.drawable.high_rating,
                            isActive = state.feedbackTagFilter[1]
                        )
                        SellerHint(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForUserEvent.FeedbackTagClick(2))
                            },
                            text = "Низкий рейтинг",
                            leftImageId = R.drawable.low_rating,
                            isActive = state.feedbackTagFilter[2]
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        for (i in 0..min(countFeedbackItem - 1, state.filteredFeedback.size - 1))
                            Feedback(
                                feedbackData = state.filteredFeedback[i]
                            )
                    }
                    if ( state.filteredFeedback.size > countFeedbackItem )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                            ,
                            text = "Показать еще",
                            isGray = true,
                            onClick = {
                                countFeedbackItem += 3
                            }
                        )
                }
            }
            item{
                Spacer( modifier = Modifier.height(50.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 2000)
@Composable
private fun Preview() {
    SellerProfileForUserScreen(
        state = SellerProfileForUserState(
            isShowAdditional = false,
            user = SellerData(
                name = "Артур",
                nick = "@bibo",
                imageId = R.drawable.seller,
                rate = 5.0f,
                profession = "Художник",
                about = "Описание описание описание описание описание описание описание описание ",
                skills = List(7) {item -> "Подсказка"},
            ),
            portfolio = List(11) {
                PortfolioItemData(
                    id = 1L,
                    media = listOf(R.drawable.background, R.drawable.background),
                    title = "Антон",
                    description = "Антон",
                    tags = listOf(
                        "Видео", "Не видео"
                    )
                )
            },
            filteredFeedback = List(10){ item ->
                FeedbackData(
                    user = UserData(
                        name = "Гена",
                        nick = "@df",
                        imageId = R.drawable.seller,
                        rate = 5.0f,
                    ),
                    text = "Отзыв отзыв",
                    starCount = 5,
                    date = LocalDateTime.of(2022, 12, 11, 11, 11)
                )
            },
            allPortfolioTags = listOf("Все", "Видео", "Не видео", "Фото", "Не фото", "Попа"),
            portfolioTagFilter = List(6) { index -> index == 0 },

        )
    )
}
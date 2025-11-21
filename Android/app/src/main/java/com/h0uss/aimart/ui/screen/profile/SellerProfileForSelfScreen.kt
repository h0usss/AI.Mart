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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.h0uss.aimart.ui.assets.Balance
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Dropdown
import com.h0uss.aimart.ui.assets.Feedback
import com.h0uss.aimart.ui.assets.Hint
import com.h0uss.aimart.ui.assets.SellerHint
import com.h0uss.aimart.ui.assets.card.PortfolioCard
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Black90
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfState
import java.time.LocalDateTime
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForSelfScreen(
    modifier: Modifier = Modifier,
    state: SellerProfileForSelfState = SellerProfileForSelfState(),
    onEvent: (SellerProfileForSelfEvent) -> Unit = {},
) {
    val chunkedProducts = state.portfolio.chunked(2)
    var countPortfolioRowItem by remember{ mutableIntStateOf(2) }
    var countFeedbackItem by remember{ mutableIntStateOf(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(top = 11.dp)
        ,
    ){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Dropdown(
                        isSettings = true,
                        isVisible = state.isVisibleSettings,
                        offset = DpOffset(
                            x = (-40).dp,
                            y = (0).dp
                        ),
                        onItemClick = { id ->
                            onEvent(SellerProfileForSelfEvent.ShowSettingsMenu)
                        },
                        onDismissRequest = {
                            onEvent(SellerProfileForSelfEvent.DismissSettingsMenu)
                        }
                    )
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
                            modifier = Modifier.padding(16.dp),
                            text = "Редактировать профиль",
                            isGray = true,
                            onClick = {
                                onEvent(SellerProfileForSelfEvent.EditClick)
                            },
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 12.dp, end = 16.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = painterResource(R.drawable.star),
                                contentDescription = "Settings"
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = state.user.rate.toString(),
                                style = semiboldStyle,
                                fontSize = 16.sp,
                                color = Black100
                            )
                        }
                        Image(
                            modifier = Modifier
                                .clickable {
                                    onEvent(SellerProfileForSelfEvent.ShowSettingsMenu)
                                },
                            painter = painterResource(R.drawable.setting),
                            contentDescription = "Settings"
                        )
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Balance(
                        balance = state.user.balance,
                        onEmptiedClick = {
                            onEvent(SellerProfileForSelfEvent.EmptiedAccountClick)
                        },
                        onReplenishClick = {
                            onEvent(SellerProfileForSelfEvent.ReplenishAccountClick)
                        },
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
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Портфолио",
                            style = semiboldStyle,
                            fontSize = 18.sp,
                            color = Black80
                        )
                        Image(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForSelfEvent.AddCaseClick)
                            },
                            painter = painterResource(R.drawable.plus),
                            contentDescription = "Plus"
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        itemsIndexed(state.allPortfolioTags){ index, item ->
                            SellerHint(
                                modifier = Modifier.clickable{
                                    onEvent(SellerProfileForSelfEvent.PortfolioTagClick(item))
                                },
                                text = item,
                                isActive = state.portfolioFilter[index]
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
                                    portfolioData = portfolio,
                                    onClick = {
                                        onEvent(SellerProfileForSelfEvent.PortfolioItemClick(portfolio.id))
                                    },
                                    onTrashClick = {
                                        onEvent(SellerProfileForSelfEvent.DeleteCaseClick(portfolio.id))
                                    },
                                )
                            }
                            if (chunkedProducts[i].size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    if (chunkedProducts.size > countPortfolioRowItem)
                        Button(
                            modifier = Modifier.padding(start = 32.dp, top = 16.dp, end = 32.dp),
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
                                onEvent(SellerProfileForSelfEvent.FeedbackTagClick(0))
                            },
                            text = "Все",
                            isActive = state.feedbackFilter[0]
                        )
                        SellerHint(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForSelfEvent.FeedbackTagClick(1))
                            },
                            text = "Высокий рейтинг",
                            leftImageId = R.drawable.high_rating,
                            isActive = state.feedbackFilter[1]
                        )
                        SellerHint(
                            modifier = Modifier.clickable{
                                onEvent(SellerProfileForSelfEvent.FeedbackTagClick(2))
                            },
                            text = "Низкий рейтинг",
                            leftImageId = R.drawable.low_rating,
                            isActive = state.feedbackFilter[2]
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
                            modifier = Modifier.padding(top = 24.dp),
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
    SellerProfileForSelfScreen(
        state = SellerProfileForSelfState(
            isVisibleSettings = false,
            user = SellerData(
                name = "Артур",
                nick = "@bibo",
                imageId = R.drawable.seller,
                rate = 5.0f,
                profession = "Художник",
                about = "Описание описание описание описание описание описание описание описание ",
                skills = List(7) {item -> "Подсказка"},
            ),
            portfolio = List(11){
                PortfolioItemData(
                    id = 1L,
                    imageId = R.drawable.background,
                    name = "Антон",
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
            allPortfolioTags = listOf("Все", "Видео", "Не видео", "Фото", "Не фото", "Попа" ),
            portfolioFilter = List(6) { index -> index == 0 },
        )
    )
}
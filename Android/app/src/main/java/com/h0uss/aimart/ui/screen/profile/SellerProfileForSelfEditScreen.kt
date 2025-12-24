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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.AlertData
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.data.model.SellerData
import com.h0uss.aimart.ui.assets.AddTag
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Dropdown
import com.h0uss.aimart.ui.assets.Hint
import com.h0uss.aimart.ui.assets.SellerHint
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.assets.card.PortfolioCard
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Black90
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfEditEvent
import com.h0uss.aimart.ui.viewModel.profile.SellerProfileForSelfEditState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellerProfileForSelfEditScreen(
    modifier: Modifier = Modifier,
    state: SellerProfileForSelfEditState = SellerProfileForSelfEditState(),
    onEvent: (SellerProfileForSelfEditEvent) -> Unit = {},
) {
    val chunkedProducts = state.portfolio.chunked(2)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
        ,
    ){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
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
                            onEvent(SellerProfileForSelfEditEvent.ShowSettingsMenu)
                        },
                        onDismissRequest = {
                            onEvent(SellerProfileForSelfEditEvent.DismissSettingsMenu)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                            ,
                            text = "Сохранить",
                            onClick = {
                                onEvent(SellerProfileForSelfEditEvent.SaveClick)
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
                                    onEvent(SellerProfileForSelfEditEvent.ShowSettingsMenu)
                                },
                            painter = painterResource(R.drawable.setting),
                            contentDescription = "Settings"
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
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
                    TextField(
                        modifier = Modifier.padding(top = 16.dp),
                        isFocus = false,
                        isBigTextField = true,
                        placeHolder = "Напишите тут информацию о себе ..",
                        state = state.newAboutState,
                        radiusPercent = 10,
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
                    AddTag(
                        modifier = Modifier.padding(top = 16.dp),
                        placeHolder = "7 - максимально",
                        state = state.newSkillState,
                        onClickAdd = {
                            if (state.user.skills.size < 7)
                                onEvent(SellerProfileForSelfEditEvent.AddSkillClick(
                                    state.newSkillState.text.toString()
                                ))
                        },
                    )
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
                    }

                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        itemsIndexed(state.allPortfolioTags){ index, item ->
                            SellerHint(
                                modifier = Modifier.clickable{
                                    onEvent(SellerProfileForSelfEditEvent.PortfolioTagClick(item))
                                },
                                text = item,
                                isActive = state.portfolioFilter[index]
                            )
                        }
                    }

                    chunkedProducts.forEach { portfolio ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, top = 16.dp, end = 32.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                val portfolio1 = portfolio[0]
                                PortfolioCard(
                                    portfolioData = portfolio1,
                                    onClick = {
                                        onEvent(
                                            SellerProfileForSelfEditEvent.PortfolioItemClick(
                                                portfolio1.id
                                            )
                                        )
                                    },
                                    onTrashClick = {
                                        onEvent(SellerProfileForSelfEditEvent.ShowAlert(
                                            AlertData(
                                                title = "Вы уверены, что хотите удалить кейс?",
                                                leftText = "Удалить",
                                                rightText = "Отменить",
                                                rightClick = { onEvent(SellerProfileForSelfEditEvent.DeleteAlert) },
                                                leftClick = {
                                                    onEvent(SellerProfileForSelfEditEvent.DeleteAlert)
                                                    onEvent(SellerProfileForSelfEditEvent.DeleteCaseClick(
                                                        portfolio1.id
                                                    ))
                                                },
                                            )
                                        ))
                                    }
                                )
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                if (portfolio.size > 1) {
                                    val portfolio2 = portfolio[1]
                                    PortfolioCard(
                                        portfolioData = portfolio2,
                                        onClick = {
                                            onEvent(
                                                SellerProfileForSelfEditEvent.PortfolioItemClick(
                                                    portfolio2.id
                                                )
                                            )
                                        },
                                        onTrashClick = {
                                            onEvent(
                                                SellerProfileForSelfEditEvent.DeleteCaseClick(
                                                    portfolio2.id
                                                )
                                            )
                                        }
                                    )
                                } else {
                                    Spacer(Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, top = 16.dp, end = 32.dp)
                        ,
                        text = "Добавить кейс",
                        isGray = true,
                        rightImageId = R.drawable.plus,
                        onClick = {
                            onEvent(SellerProfileForSelfEditEvent.AddCaseClick)
                        },
                    )
                }
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 18.dp, end = 32.dp)
                    ,
                    text = "Выйти",
                    isRedBorder = true,
                    onClick = {
                        onEvent(SellerProfileForSelfEditEvent.ShowAlert(
                                AlertData(
                                    title = "Вы уверены, что хотите выйти?",
                                    leftText = "Выйти",
                                    rightText = "Отменить",
                                    rightClick = { onEvent(SellerProfileForSelfEditEvent.DeleteAlert) },
                                    leftClick = {
                                        onEvent(SellerProfileForSelfEditEvent.DeleteAlert)
                                        onEvent(SellerProfileForSelfEditEvent.ExitClick)
                                    },
                                )
                            )
                        )
                    },
                    leftImageId = R.drawable.exit
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 12.dp, end = 32.dp)
                    ,
                    text = "Удалить аккаунт",
                    isRedFill = true,
                    onClick = {
                        onEvent(SellerProfileForSelfEditEvent.ShowAlert(
                                AlertData(
                                    title = "Вы уверены, что хотите удалить аккаунт?",
                                    description = "После удаления аккаунта ваши данные восстановить невозможно",
                                    leftText = "Удалить",
                                    rightText = "Отменить",
                                    rightClick = { onEvent(SellerProfileForSelfEditEvent.DeleteAlert) },
                                    leftClick = {
                                        onEvent(SellerProfileForSelfEditEvent.DeleteAlert)
                                        onEvent(SellerProfileForSelfEditEvent.DeleteAccountClick)
                                    },
                                )
                            )
                        )
                    },
                    leftImageId = R.drawable.white_trash
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 2000)
@Composable
private fun Preview() {
    SellerProfileForSelfEditScreen(
        state = SellerProfileForSelfEditState(
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
            allPortfolioTags = listOf("Все", "Видео", "Не видео", "Фото", "Не фото", "Попа" ),
            portfolioFilter = List(6) { index -> index == 0 }
        )
    )
}
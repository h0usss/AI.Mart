package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.ui.assets.NotificationsCount
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.mediumStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import kotlinx.coroutines.launch

@Composable
fun MyProducts(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    balance: String
) {
    val tabs = listOf("Ожидание", "Активные", "Архив")
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(White)
            .padding(horizontal = 16.dp, vertical = 65.dp)
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Объявления",
            style = semiboldStyle,
            color = Black80,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .padding(top = 18.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Black10,
                    shape = RoundedCornerShape(30)
                )
                .padding(16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Баланс",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black80
            )
            Text(
                text = "$$balance",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black80
            )
        }

        SecondaryTabRow (
            modifier = Modifier.padding(top = 22.dp, bottom = 100.dp),
            selectedTabIndex = pagerState.currentPage,
            containerColor = White,
        ){
            tabs.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(bottom = 5.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = "${pagerState.currentPage}",
                        style = mediumStyle,
                        fontSize = 14.sp,
                        color =
                            if (index == pagerState.currentPage) Teal
                            else Black80
                    )
                    NotificationsCount(
                        count = 1,
                        isActive = index == pagerState.currentPage
                    )
                }
            }
        }

    }
}


@Preview
@Composable
private fun Preview() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    MyProducts(
        pagerState = pagerState,
        balance = "1,000",
    )
}
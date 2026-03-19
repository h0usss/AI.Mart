package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.AddData
import com.h0uss.aimart.ui.theme.Black20
import com.h0uss.aimart.ui.theme.Black80


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddBanner(
    modifier: Modifier = Modifier,
    adds: List<AddData>,
) {
    if (adds.isEmpty()) return

    val pageCount = adds.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(136.dp)
            .clip(RoundedCornerShape(5))
    ) {

        HorizontalPager(
            state = pagerState,
        ) { page ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{},
                painter = painterResource(adds[page].image),
                contentDescription = adds[page].name,
                contentScale = ContentScale.FillWidth
            )
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp, start = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Black80
                    else Black20
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AddBanner(
        adds = List(5) { item ->
            AddData(
                image = R.drawable.add_0,
                url = "",
                name = ""
            )
        }
    )
}
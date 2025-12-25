package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun ShowPortfolio(
    modifier: Modifier = Modifier,
    data: PortfolioItemData,
    onExit: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable{ onExit() }
            .padding(horizontal = 16.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(16.dp)
                .clickable(enabled = false){}
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data.media) { media ->
                    Image(
                        modifier = Modifier
                            .height(240.dp)
                            .clip(RoundedCornerShape(8.dp))
                        ,
                        painter = painterResource(media),
                        contentDescription = "Portfolio image $media",
                        contentScale = ContentScale.FillHeight
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                ,
                text = data.title,
                style = semiboldStyle,
                color = Black80,
                fontSize = 16.sp,
            )
            Text(
                modifier = Modifier
                    .padding(top = 12.dp)
                ,
                text = data.description,
                style = regularStyle,
                color = Black80,
                fontSize = 14.sp,
            )
        }

        Image(
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable{ onExit() }
            ,
            painter = painterResource(R.drawable.close_portfolio),
            contentDescription = "Close portfolio"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ShowPortfolio(
        data = PortfolioItemData(
            id = 0,
            title = "Заголовок",
            description = "Какое-то очень длинное описание проделанной работы. Вот тут я делала этот экран потому что устала прокрастинировать и ничего не делать а дизайном заиматься надо. Как думаешь, текст один длинный оставить или присвайпе картинки менять?\n\nесли оставить длинный то надо ограничение на количесвто символов",
            media = listOf(
                R.drawable.add_0,
                R.drawable.add_0,
                R.drawable.add_0,
            ),
            tags = emptyList()
        )
    )
}
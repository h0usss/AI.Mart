package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import kotlin.math.floor

@Composable
fun PlaceAnOrder(
    modifier: Modifier = Modifier,
    user: UserData,
    onUserClick: (Long) -> Unit = {},
    onBuyClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(16.dp)
            )
            .background(White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    onUserClick(user.id)
                }
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    model = user.imageUrl,
                    contentDescription = "User ${user.id}",
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = user.name,
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user.rate.toString(),
                            style = regularStyle,
                            fontSize = 12.sp,
                            color = Black50
                        )
                        Row(
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            for (i in 1..floor(user.rate).toInt())
                                Image(
                                    painter = painterResource(R.drawable.star_yellow_feedback),
                                    contentDescription = "Star yellow"
                                )
                            for (i in 1..5 - floor(user.rate).toInt())
                                Image(
                                    painter = painterResource(R.drawable.star_black_feedback),
                                    contentDescription = "Star black"
                                )
                        }
                    }
                }
            }
            Image(
                modifier = Modifier.padding(6.dp),
                painter = painterResource(R.drawable.small_arrow),
                contentDescription = "Small arrow"
            )
        }
        Row(
            modifier = modifier
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f),
                text = "Оформить заказ",
                onClick = {
                    onBuyClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PlaceAnOrder(
        user = UserData(
            name = "Бобо",
            rate = 2f,
        )
    )
}
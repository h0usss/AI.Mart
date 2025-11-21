package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun StatisticBox(
    modifier: Modifier = Modifier,
    imageId: Int,
    num: String,
    name: String
) {
    Column(
        modifier = modifier
            .height(84.dp)
            .defaultMinSize(minWidth = 100.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(White)
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(15.dp)
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier.padding(end = 4.dp),
                painter = painterResource(imageId),
                contentDescription = name
            )
            Text(
                modifier = Modifier,
                text = num,
                style = semiboldStyle,
                fontSize = 14.sp,
                color = Black100
            )
        }
        Text(
            modifier = Modifier,
            text = name,
            style = regularStyle,
            fontSize = 14.sp,
            color = Black100
        )
    }
}

@Preview
@Composable
private fun Preview() {
    StatisticBox(
        imageId = R.drawable.star,
        num = "5,0",
        name = "Рейтинг",
    )
}
package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Balance(
    modifier: Modifier = Modifier,
    balance: String,
    onEmptiedClick: () -> Unit = {},
    onReplenishClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(15.dp)
            )
            .background(White)
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                modifier = Modifier,
                text = "Баланс",
                style = semiboldStyle,
                fontSize = 18.sp,
                color = Black80
            )
            Text(
                modifier = Modifier,
                text = "$$balance",
                style = semiboldStyle,
                fontSize = 18.sp,
                color = Black80
            )
        }
        Row(
            modifier = modifier
                .padding(top = 16.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Button(
                modifier = Modifier
                    .weight(.5f)
                ,
                text = "Вывести",
                isGray = true,
                onClick = {
                    onEmptiedClick()
                }
            )
            Button(
                modifier = Modifier
                    .weight(.5f)
                ,
                text = "Пополнить",
                onClick = {
                    onReplenishClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Balance(
        balance = "1,000"
    )
}
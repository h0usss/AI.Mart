package com.h0uss.aimart.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.TopUpWalletEvent

@Composable
fun TopUpWalletScreen(
    modifier: Modifier = Modifier,
    onEvent: (TopUpWalletEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .background(White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = "Пополнить кошелек",
                style = semiboldStyle,
                fontSize = 18.sp,
                color = Black80
            )
            Image(
                modifier = Modifier
                    .clickable{
                      onEvent(TopUpWalletEvent.Exit)
                    },
                painter = painterResource(R.drawable.x_40),
                contentDescription = "X",
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 85.dp)
                .clickable {
                    onEvent(TopUpWalletEvent.TopUp)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.bank),
                    contentDescription = "Bank",
                )
                Text(
                    text = "С моего счета в другом банке",
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black80
                )
            }
            Image(
                modifier = Modifier.padding(10.dp),
                painter = painterResource(R.drawable.small_arrow),
                contentDescription = "Next",
            )
        }
    }
}

@Preview
@Composable
private fun Preview_v1() {
    TopUpWalletScreen()
}
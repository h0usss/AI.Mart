package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun SuccessNewOrder(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable { onExit() }
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(16.dp)
                .clickable(enabled = false) {}
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                text = "Ваш заказ отправлен!\nОжидайте подтверждение\nот продавца.",
                style = semiboldStyle,
                color = Black80,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onExit() }
                ,
                painter = painterResource(R.drawable.success_order),
                contentDescription = "Success order"
            )

        }

        Image(
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable{ onExit() }
            ,
            painter = painterResource(R.drawable.close_portfolio),
            contentDescription = "Close order"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SuccessNewOrder()
}
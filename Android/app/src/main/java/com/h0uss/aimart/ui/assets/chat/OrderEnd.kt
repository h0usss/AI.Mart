package com.h0uss.aimart.ui.assets.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderEnd(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    canPay: Boolean,
    onPayClick: () -> Unit = {},
    onOpenTicketClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ,
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = "Заказ заврешен продавцом",
            style = semiboldStyle,
            fontSize = 16.sp,
            color = Black80
        )
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
            ,
            text = "Нажимая кнопку “Оплатить”, " +
                    "Вы подтверждаете что заказ завершен, и деньги отправляются продавцу.\n\n" +
                    "Если Вы считаете, что услуга не предоставлена, вы можете открыть спор.",
            style = regularStyle,
            fontSize = 14.sp,
            color = Black80,
            textAlign = TextAlign.Justify,
            lineHeight = 20.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Итого",
                style = semiboldStyle,
                fontSize = 18.sp,
                color = Black80
            )
            Text(
                text = "${orderData.price}$",
                style = semiboldStyle,
                fontSize = 18.sp,
                color = Black80
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            ,
            text = "Оплатить",
            isGray = !canPay,
            onClick = {
                if (canPay)
                    onPayClick()
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            ,
            text = "Открыть спор",
            isRedFill = true,
            onClick = {
                onOpenTicketClick()
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v1() {
    OrderEnd(
        modifier = Modifier
            .background(White),
        orderData = OrderData(
            price = 11.2f
        ),
        canPay = true,
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_v2() {
    OrderEnd(
        modifier = Modifier
            .background(White),
        orderData = OrderData(
            price = 11.2f
        ),
        canPay = false
    )
}
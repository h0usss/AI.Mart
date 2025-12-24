package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.data.model.AlertData
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Alert(
    modifier: Modifier = Modifier,
    data: AlertData,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(White)
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 24.dp)
    ){
        Text(
            modifier = Modifier
                .fillMaxWidth()
            ,
            text = data.title,
            style = semiboldStyle,
            color = Black80,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        if (data.description.isNotEmpty()){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                ,
                text = data.description,
                style = regularStyle,
                color = Black80,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Button(
                modifier = Modifier.weight(1f),
                text = data.leftText,
                isWhite = true,
                onClick = data.leftClick,
            )
            Button(
                modifier = Modifier.weight(1f),
                text = data.rightText,
                onClick = data.rightClick,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(100.dp)
    ){
        Alert(
            data = AlertData(
                title = "Вы уверены, что хотите удалить аккаунт?",
                description = "После удаления аккаунта ваши данные восстановить невозможно",
                leftText = "Удалить",
                rightText = "Отменить",
            )
        )
        Alert(
            data = AlertData(
                title = "Вы уверены, что хотите выйти?",
                leftText = "Выйти",
                rightText = "Отменить",
            )
        )
        Alert(
            data = AlertData(
                title = "Вы уверены, что хотите удалить кейс?",
                leftText = "Выйти",
                rightText = "Отменить",
            )
        )
    }
}
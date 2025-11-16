package com.h0uss.aimart.ui.screen.authorize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black45
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.agreementAnnotatedString
import com.h0uss.aimart.ui.theme.haveAccountAnnotatedString
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun SignUp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .padding(start = 24.dp, top = 72.dp, end = 24.dp )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "AI Mart",
            color = Black100,
            fontSize = 24.sp,
            style = semiboldStyle,
        )
        Text(
            modifier = Modifier
                .padding(top = 48.dp)
            ,
            text = "Регистрация",
            color = Black100,
            fontSize = 18.sp,
            style = semiboldStyle,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
            ,
            text = "Имя",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "Ваше имя"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "E-mail",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "example@gmail.com"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "Дата рождения",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "DD/MM/YYYY",
            rightImageId = R.drawable.calendar,
            onClickRightImage = {}
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "Пароль",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "Пароль от 8 символов"
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            text = "Продолжить",
            rightImageId = R.drawable.arrow,
            onClick = {}
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            HorizontalDivider(
                modifier = Modifier
                    .width(143.dp)
                ,
                color = Black10
            )
            Text(
                text = "или",
                color = Black45,
                fontSize = 14.sp,
                style = regularStyle,
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(143.dp)
                ,
                color = Black10
            )
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            text = "Использовать",
            isGray = true,
            onClick = {},
            leftImageId = R.drawable.google
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text =  buildAnnotatedString {
                agreementAnnotatedString(
                    isLogin = false,
                    onClickAgreement = {},
                    onClickPrivacyPolicy = {}
                )
            },
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text =  buildAnnotatedString {
                haveAccountAnnotatedString(
                    onClickLogin = {}
                )
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    SignUp()
}
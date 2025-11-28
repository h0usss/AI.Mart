package com.h0uss.aimart.ui.screen.authorize

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.agreementAnnotatedString
import com.h0uss.aimart.ui.theme.noAccountAnnotatedString
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.authorize.SignInEvent
import com.h0uss.aimart.ui.viewModel.authorize.SignInState
import com.h0uss.aimart.util.inputTransformation.emailInputTransformation
import com.h0uss.aimart.util.inputTransformation.passwordInputTransformation
import com.h0uss.aimart.util.outputTransformation.passwordOutputTransformation

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    state: SignInState = SignInState(),
    onEvent: (SignInEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 24.dp, top = 150.dp, end = 24.dp )
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
                .padding(top = 25.dp)
            ,
            text = "Вход",
            color = Black100,
            fontSize = 18.sp,
            style = semiboldStyle,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
            ,
            text = "E-mail/username",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 4.dp)
            ,
            placeHolder = "example@gmail.com",
            state = state.emailState,
            inputTransformation = emailInputTransformation(),
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
            ,
            text = "Пароль",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 4.dp)
            ,
            state = state.passwordState,
            placeHolder = "Введите пароль",
            inputTransformation = passwordInputTransformation(),
            outputTransformation = passwordOutputTransformation(),
        )
        if (state.authError != null)
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource( R.drawable.error_status ),
                    contentDescription = "Error"
                )
                Text(
                    text = " ${state.authError}",
                    fontSize = 12.sp,
                    style = regularStyle,
                    color = ErrorText,
                )
            }
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            text = "Войти",
            onClick = {
                onEvent(SignInEvent.SignInClicked)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            HorizontalDivider(
                modifier = Modifier
                    .width(80.dp)
                ,
                color = Black10
            )
            Text(
                text = "или войти с помощью",
                color = Black45,
                fontSize = 14.sp,
                style = regularStyle,
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(80.dp)
                ,
                color = Black10
            )
        }
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
            ,
            text = "Войти",
            isGray = true,
            onClick = {
                onEvent(SignInEvent.GoogleSignInClicked)
            },
            leftImageId = R.drawable.google
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text =  buildAnnotatedString {
                agreementAnnotatedString(
                    isLogin = true,
                    onClickAgreement = {
                        onEvent(SignInEvent.AgreementClicked)
                    },
                    onClickPrivacyPolicy = {
                        onEvent(SignInEvent.PrivacyPolicyClicked)
                    }
                )
            },
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text =  buildAnnotatedString {
                noAccountAnnotatedString(
                    onClickRegister = {
                        onEvent(SignInEvent.RegisterClicked)
                    }
                )
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    SignInScreen(
        state = SignInState(
        authError = "Неверный логин или пароль"
        )
    )
}
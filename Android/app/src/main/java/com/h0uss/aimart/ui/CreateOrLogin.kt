package com.h0uss.aimart.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.assets.Button

@Composable
fun CreateOrLogin(
    modifier: Modifier = Modifier,
    isSplashSender: Boolean = true,
    navToLogin: () -> Unit = {},
    navToRegister: () -> Unit = {},
) {
    var startAnimation by remember { mutableStateOf(!isSplashSender) }

    LaunchedEffect(key1 = Unit) {
        if (isSplashSender) {
            startAnimation = true
        }
    }

    val offsetBg by animateDpAsState(
        targetValue = if (startAnimation) (-300).dp else 100.dp,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "offsetBg"
    )

    val offsetIcon by animateDpAsState(
        targetValue = if (startAnimation) (-100).dp else (0).dp,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "offsetIcon"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2000.dp)
            .paint(
                painter = painterResource(R.drawable.background_for_create_or_login),
                contentScale = ContentScale.FillWidth,
            )

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .offset(y = offsetBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.create_or_login_background),
                    contentDescription = "Create or login background",
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.BottomCenter,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ){
            Image(
                modifier = Modifier
                    .offset(y = offsetIcon)
                    .align(Alignment.Center),
                painter = painterResource(R.drawable.icon_black),
                contentDescription = "Icon dark",
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 170.dp)
            ) {
                Button(
                    text = "Войти",
                    onClick = navToLogin,
                )
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "Зарегистрироваться",
                    isGray = true,
                    onClick = navToRegister,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CreateOrLogin()
}

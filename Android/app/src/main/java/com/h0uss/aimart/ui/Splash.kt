package com.h0uss.aimart.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Teal
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt

@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Splash(
    modifier: Modifier = Modifier,
    navToNext: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    var startFirstRotation by remember { mutableStateOf(false) }
    var startSecondPhase by remember { mutableStateOf(false) }
    var isAnimationFinished by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val revealTargetRadius = sqrt(screenWidthPx.pow(2) + screenHeightPx.pow(2))


    val revealRadius by animateFloatAsState(
        targetValue = if (startSecondPhase) revealTargetRadius else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "revealRadius"
    )

    val firstRotation by animateFloatAsState(
        targetValue = if (startFirstRotation) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "firstRotation"
    )

    val secondRotation by animateFloatAsState(
        targetValue = if (startSecondPhase) -180f else 0f,
        animationSpec = tween(durationMillis = 900, easing = LinearOutSlowInEasing),
        label = "secondRotation"
    )

    LaunchedEffect(Unit) {
        delay(700)
        startFirstRotation = true

        delay(700)
        startSecondPhase = true

        delay(1000)
        isAnimationFinished = true
    }

    LaunchedEffect(isAnimationFinished) {
        if (isAnimationFinished) {
            if (authUserIdLong != -1L) navToHome() else navToNext()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black100)
            .drawBehind {
                drawCircle(
                    color = Teal,
                    radius = revealRadius
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = !startSecondPhase,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 0))
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationY = firstRotation
                        }
                    ,
                    painter = painterResource(R.drawable.icon_light),
                    contentDescription = null,
                )
            }

            AnimatedVisibility(
                visible = startSecondPhase,
                enter = fadeIn(animationSpec = tween(durationMillis = 0))
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationY = secondRotation
                            scaleX = -1f
                        }
                    ,
                    painter = painterResource(R.drawable.icon_black),
                    contentDescription = null,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    Splash()
}

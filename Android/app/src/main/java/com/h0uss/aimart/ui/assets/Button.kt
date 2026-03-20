package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Complete
import com.h0uss.aimart.ui.theme.Debate
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    isGray: Boolean = false,
    isWhite: Boolean = false,
    isRedBorder: Boolean = false,
    isRedFill: Boolean = false,
    isGreen: Boolean = false,
    onClick: () -> Unit,
    leftImageId: Int = -1,
    rightImageId: Int = -1,
) {
    Box(
        modifier = modifier
//            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20))
            .background(
                if (isGray) Black5
                else if (isGreen) Complete
                else if (isRedBorder || isWhite) White
                else if (isRedFill) Debate
                else Teal
            )
            .border(
                width = 1.dp,
                color =
                    if (isGray) Black5
                    else if (isWhite) Black10
                    else if (isRedBorder || isRedFill) Debate
                    else Teal,
                shape = RoundedCornerShape(20)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (leftImageId != -1)
                Image(
                    painter = painterResource(leftImageId),
                    contentDescription = "Arrow"
                )

            Text(
                text = text,
                fontSize = 14.sp,
                color =
                    if (isGray || isWhite) Black80
                    else if (isRedBorder) Debate
                    else White,
                style = semiboldStyle,
            )

            if (rightImageId != -1)
                Image(
                    painter = painterResource(rightImageId),
                    contentDescription = "Arrow"
                )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
            text = "Войти",
            onClick = {}
        )
        Button(
            text = "Войти",
            onClick = {},
            isGray = true,
            leftImageId = R.drawable.google
        )
        Button(
            text = "Войти",
            onClick = {},
            rightImageId = R.drawable.arrow
        )
        Button(
            text = "Войти",
            onClick = {},
            isRedBorder = true,
            rightImageId = R.drawable.arrow
        )
        Button(
            text = "Войти",
            onClick = {},
            isRedFill = true,
            rightImageId = R.drawable.arrow
        )
    }
}
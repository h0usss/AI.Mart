package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import com.h0uss.aimart.ui.theme.mediumStyle

@Composable
fun Hint(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = Black10,
                shape = RoundedCornerShape(50)
            )
            .background(White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ){
        Text(
            text = text,
            style = mediumStyle,
            fontSize = 14.sp,
            color = Black80
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Hint(
        text = "Хало"
    )
}
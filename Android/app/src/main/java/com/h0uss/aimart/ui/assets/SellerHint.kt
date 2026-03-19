package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.mediumStyle

@Composable
fun SellerHint(
    modifier: Modifier = Modifier,
    text: String,
    isFill: Boolean = false,
    isActive: Boolean = false,
    leftImageId: Int = -1
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color =
                    if (isActive) Teal
                    else Black10,
                shape = CircleShape
            )
            .background(
                if (isFill) Black5
                else if (isActive) Teal
                else White
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        if (leftImageId != -1)
            Image(
                modifier = Modifier.padding(end = 4.dp),
                painter = painterResource(leftImageId),
                contentDescription = "Left image"
            )
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = text,
            style = mediumStyle,
            fontSize = 14.sp,
            color =
                if (isActive) White
                else Black80
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Row {
        SellerHint(
            text = "Хало"
        )
        SellerHint(
            text = "Хало",
            isActive = true
        )
        SellerHint(
            text = "Низкий рейтинг",
            leftImageId = R.drawable.low_rating
        )

    }
}
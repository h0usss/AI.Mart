package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import com.h0uss.aimart.ui.theme.mediumStyle

@Composable
fun RecentSearchTerm(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit = {},
    onXClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .defaultMinSize(minWidth = 70.dp)
            .background(Black5)
            .border(
                width = 1.dp,
                color = Black10,
                shape = CircleShape
            )
            .padding(start = 8.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
            .clickable{ onClick() }
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            modifier = Modifier,
            text = name,
            style = mediumStyle,
            fontSize = 14.sp,
            color = Black80
        )
        Image(
            modifier = Modifier
                .padding(start = 2.dp)
                .clickable{ onXClick() },
            painter = painterResource(R.drawable.x),
            contentDescription = "X"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    RecentSearchTerm(
        name = "mi"
    )
}
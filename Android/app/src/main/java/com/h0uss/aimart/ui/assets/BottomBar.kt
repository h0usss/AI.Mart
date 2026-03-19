package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.White

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 36.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Image(
            painter = painterResource(R.drawable.bottom_home),
            contentDescription = "Home"
        )
        Image(
            painter = painterResource(R.drawable.bottom_chat),
            contentDescription = "Chat"
        )
        Image(
            painter = painterResource(R.drawable.bottom_products),
            contentDescription = "Products"
        )
        Image(
            painter = painterResource(R.drawable.bottom_job),
            contentDescription = "Job"
        )
        Image(
            painter = painterResource(R.drawable.bottom_profile),
            contentDescription = "Profile"
        )
    }
}


@Preview()
@Composable
private fun Preview() {
    BottomBar()
}
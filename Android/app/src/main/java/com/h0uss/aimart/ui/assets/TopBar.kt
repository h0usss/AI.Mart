package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.White

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAdditionalClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Image(
            modifier = Modifier
                .clickable{
                    onBackClick()
                }
            ,
            painter = painterResource(R.drawable.back),
            contentDescription = "Back"
        )
        Image(
            modifier = Modifier
                .clickable{
                    onAdditionalClick()
                }
            ,
            painter = painterResource(R.drawable.additional),
            contentDescription = "Additional"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TopBar(

    )
}
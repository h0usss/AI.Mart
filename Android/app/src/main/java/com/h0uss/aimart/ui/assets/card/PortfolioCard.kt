package com.h0uss.aimart.ui.assets.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun PortfolioCard(
    modifier: Modifier = Modifier,
    productName: String,
    productImageId: Int,
    onClick: () -> Unit = {},
    onTrashClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(White)
            .clickable{
                onClick()
            }
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopEnd
        ){
            Image(
                modifier = Modifier
                    .size(164.dp)
                    .clip(RoundedCornerShape(5))
                ,
                painter = painterResource(productImageId),
                contentDescription = "Product image",
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
                    .clickable{
                        onTrashClick()
                    }
                ,
                painter = painterResource(R.drawable.trash),
                contentDescription = "Trash"
            )
        }
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = productName,
            style = regularStyle,
            fontSize = 14.sp,
            color = Black100
        )
    }
}


@Preview
@Composable
private fun Preview() {
    PortfolioCard(
        productName = "ProductName",
        productImageId = R.drawable.background
    )
}
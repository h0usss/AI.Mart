package com.h0uss.aimart.ui.assets.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: ProductCardData,
    onClick: (Long) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(White)
            .clickable {
                onClick(product.id)
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
                painter = painterResource(product.imageId),
                contentDescription = "Product image",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = product.authorName,
            style = regularStyle,
            fontSize = 12.sp,
            color = Black50
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = product.name,
            style = regularStyle,
            fontSize = 14.sp,
            color = Black100
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = "$${product.price}",
            style = semiboldStyle,
            fontSize = 16.sp,
            color = Black100
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Row{
        ProductCard(
            modifier = Modifier.padding(end = 5.dp),
            product = ProductCardData(
                id = 1L,
                authorName = "Чуча",
                name = "ProductName",
                price = 10.99f,
                imageId = R.drawable.background,
                description = "a"
            )
        )
    }
}
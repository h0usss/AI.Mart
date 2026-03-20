package com.h0uss.aimart.ui.assets.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.ProductStatus
import com.h0uss.aimart.data.model.UserProductCardData
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun MyProductCard(
    modifier: Modifier = Modifier,
    product: UserProductCardData,
    onClick: (Long) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .clickable{
                onClick(product.id)
            }
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier.weight(.8f)
        ){
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10))
                ,
                painter = painterResource(product.imagesId[0]),
                contentDescription = product.name,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
            ){
                Text(
                    modifier = Modifier
                    ,
                    text = product.name,
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black100
                )
                Text(
                    modifier = Modifier
                        .padding(top = 6.dp)
                    ,
                    text = "$${product.price}",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black100
                )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                    ,
                    text = product.description,
                    style = regularStyle,
                    fontSize = 12.sp,
                    color = Black50,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(.2f),
            contentAlignment = Alignment.TopEnd
        ){
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = "Edit"
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyProductCard(
        product = UserProductCardData(
            id = 1L,
            name = "Пипипу",
            price = 100.00f,
            imagesId = List(4) { R.drawable.background },
            status = ProductStatus.ACTIVE,
            description = "Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки Бяки буки для запуки"
        )
    )
}
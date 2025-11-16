package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.h0uss.aimart.data.model.SellerRowData
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.BlackSellerName
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.mediumStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Sellers(
    modifier: Modifier = Modifier,
    sellers: List<SellerRowData>
) {
    Column(
        modifier = modifier
            .background(White)
            .padding(top = 14.dp)
    ){
        Row(
            modifier = Modifier.padding(start = 16.dp)
        ){
            Text(
                text = "Продавцы",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black100
            )
            Image(
                modifier = Modifier.padding(start = 16.dp),
                painter = painterResource(R.drawable.small_arrow),
                contentDescription = "Small arrow"
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(top = 22.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){
            items(sellers){ item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape),
                        painter = painterResource(item.imageId),
                        contentDescription = "Seller ${item.id}",
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = item.name,
                        style = mediumStyle,
                        fontSize = 14.sp,
                        color = BlackSellerName
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Sellers(
        sellers = listOf(
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            ),
        )
    )
}
package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.AddData
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.SellerRowData
import com.h0uss.aimart.ui.assets.AddBanner
import com.h0uss.aimart.ui.assets.Search
import com.h0uss.aimart.ui.assets.Sellers
import com.h0uss.aimart.ui.assets.card.ProductCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Home(
    modifier: Modifier = Modifier,
    add: List<AddData>,
    sellers: List<SellerRowData>,
    products: List<ProductCardData>
) {
    LazyColumn(
        modifier = modifier
            .background(White)
            .padding(top = 52.dp)
    ){
        item{
            Search(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            AddBanner(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp),
                adds = add
            )
            Sellers(
                modifier = Modifier.padding(top = 28.dp),
                sellers = sellers
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 32.dp),
                text = "Товары",
                style = semiboldStyle,
                color = Black100,
                fontSize = 16.sp
            )
        }

        val chunkedProducts = products.chunked(2)

        items(chunkedProducts) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 18.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { product ->
                    ProductCard(
                        product = product
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(85.dp))
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Home(
        add = List(5) { item ->
            AddData(
                image = R.drawable.add_0,
                url = "",
                name = ""
            )
        },
        sellers = List(10) { item ->
            SellerRowData(
                id = 0,
                name = "Пипка",
                imageId = R.drawable.nigseller
            )
        },
        products = List(21){ item ->
            ProductCardData(
                author = "Бильбо",
                name = "AI Кольцо всевластия",
                price = 0.99f,
                imageId = R.drawable.background,
                status = null
            )
        }
    )
}
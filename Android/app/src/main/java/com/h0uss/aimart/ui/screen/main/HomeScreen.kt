package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.AddData
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.UserHomeData
import com.h0uss.aimart.ui.assets.AddBanner
import com.h0uss.aimart.ui.assets.SearchTextFieldButton
import com.h0uss.aimart.ui.assets.Sellers
import com.h0uss.aimart.ui.assets.card.ProductCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.main.HomeEvent
import com.h0uss.aimart.ui.viewModel.main.HomeState
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<ProductCardData>,
    state: HomeState = HomeState(),
    onEvent: (HomeEvent) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .background(White)
            .padding(top = 11.dp)
    ){
        item{
            SearchTextFieldButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        onEvent(HomeEvent.SearchClick)
                    }
                ,
                placeHolder = "Поиск",
                leftImageId = R.drawable.loupe,
            )

            AddBanner(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp),
                adds = state.add
            )
            Sellers(
                modifier = Modifier.padding(top = 28.dp),
                sellers = state.sellers,
                onSellerClick = { sellerId ->
                    onEvent(HomeEvent.SellerClick(sellerId))
                }
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 32.dp),
                text = "Товары",
                style = semiboldStyle,
                color = Black100,
                fontSize = 16.sp
            )
        }

        val rowCount = (products.itemCount + 1) / 2
        items(
            count = rowCount,
        ) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 18.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    val product1 = products[rowIndex * 2]
                    if (product1 != null) {
                        ProductCard(
                            product = product1,
                            onClick = { productId ->
                                onEvent(HomeEvent.ProductClick(productId))
                            }
                        )
                    }
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    val product2Index = rowIndex * 2 + 1
                    if (product2Index < products.itemCount) {
                        val product2 = products[product2Index]
                        if (product2 != null) {
                            ProductCard(
                                product = product2,
                                onClick = { productId ->
                                    onEvent(HomeEvent.ProductClick(productId))
                                }
                            )
                        }
                    } else {
                        Spacer(Modifier.fillMaxWidth())
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(85.dp))
        }
    }
}


@Preview
@Composable
private fun Preview() {
    val productsFlow = flowOf(PagingData.from(List(21){ item ->
        ProductCardData(
            id = item.toLong(),
            authorName = "Бильбо",
            name = "AI Кольцо всевластия",
            price = 0.09f,
            imageId = R.drawable.background,
            description = "Ты не пройдёшь"
        )
    }))

    HomeScreen(
        state = HomeState(
            add = List(5) { item ->
                AddData(
                    image = R.drawable.add_0,
                    url = "",
                    name = ""
                )
            },
            sellers = List(10) { item ->
                UserHomeData(
                    id = 0,
                    name = "Пипка",
                    imageId = R.drawable.seller
                )
            },
        ),
        products = productsFlow.collectAsLazyPagingItems()
    )
}
package com.h0uss.aimart.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.UserHomeData
import com.h0uss.aimart.ui.assets.Hint
import com.h0uss.aimart.ui.assets.SearchTextFieldButton
import com.h0uss.aimart.ui.assets.Sellers
import com.h0uss.aimart.ui.assets.card.ProductCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.search.SearchEvent
import com.h0uss.aimart.ui.viewModel.search.SearchState
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<ProductCardData>,
    state: SearchState = SearchState(),
    onEvent: (SearchEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(White)
            .padding(top = 11.dp)
            .systemBarsPadding()
    ){
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.clickable{
                    onEvent(SearchEvent.BackClick)
                }
                ,
                painter = painterResource(R.drawable.back),
                contentDescription = "Back"
            )
            SearchTextFieldButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onEvent(SearchEvent.SearchClick)
                    }
                ,
                placeHolder = "Поиск",
                value = state.searchState.text.toString(),
                rightImageId = R.drawable.close,
                onClickRightImage = {
                    onEvent(SearchEvent.DeleteSearchClick)
                },
            )
        }

        LazyRow(
            modifier = Modifier
                .padding(top = 14.dp, bottom = 14.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.hints){ hint ->
                Hint(
                    modifier = Modifier
                        .clickable{
                            onEvent(SearchEvent.HintClick(hint))
                        }
                    ,
                    text = hint
                )
            }
        }

        if (products.itemCount == 0 && state.sellers.isEmpty()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.no_found),
                    contentDescription = "No products"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                    ,
                    text = "Упс... По Вашему запросу ничего \n" +
                            "не нашлось.\n",
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black100,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    text = "Попробуйте изменить параметры поиска.",
                    style = regularStyle,
                    fontSize = 16.sp,
                    color = Black100,
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            LazyColumn {
                item {
                    if (state.sellers.isNotEmpty())
                        Sellers(
                            modifier = Modifier.padding(top = 14.dp),
                            sellers = state.sellers,
                            onSellerClick = { sellerId ->
                                onEvent(SearchEvent.SellerClick(sellerId))
                            }
                        )
                    if (products.itemCount > 0)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 32.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Товары",
                                style = semiboldStyle,
                                color = Black100,
                                fontSize = 16.sp
                            )
                            Image(
                                painter = painterResource(R.drawable.filters),
                                contentDescription = "Filters"
                            )
                        }
                }
                if (products.itemCount > 0) {
                    val rowCount = (products.itemCount + 1) / 2
                    items(
                        count = rowCount,
                    ) { rowIndex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 18.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                val product1 = products[rowIndex * 2]
                                if (product1 != null) {
                                    ProductCard(
                                        product = product1,
                                        onClick = { productId ->
                                            onEvent(SearchEvent.ProductClick(productId))
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
                                                onEvent(SearchEvent.ProductClick(productId))
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
        }
    }
}

@Preview(widthDp = 360, heightDp = 1000)
@Composable
private fun Preview_fill_min_width() {
    Preview_fill()
}
@Preview( heightDp = 1000)
@Composable
private fun Preview_fill_norm_width() {
    Preview_fill()
}

@Preview(widthDp = 360, heightDp = 1000)
@Composable
private fun Preview_empty_min_width() {
    Preview_empty()
}
@Preview( heightDp = 1000)
@Composable
private fun Preview_empty_norm_width() {
    Preview_empty()
}

@Composable
private fun Preview_fill() {
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

    SearchResultScreen(
        products = productsFlow.collectAsLazyPagingItems(),
        state = SearchState(
            searchState = TextFieldState("Кольцо"),
            sellers = List(10) { item ->
                UserHomeData(
                    id = 0,
                    name = "Артур",
                    imageId = R.drawable.seller
                )
            },
            hints = List(10) { item -> "Подсказка" }
        )
    )
}

@Composable
private fun Preview_empty() {
    val productsFlow = flowOf(PagingData.from(listOf<ProductCardData>()))

    SearchResultScreen(
        products = productsFlow.collectAsLazyPagingItems(),
        state = SearchState(
            sellers = List(10) { item ->
                UserHomeData(
                    id = 0,
                    name = "Пипка",
                    imageId = R.drawable.seller
                )
            },
            hints = List(10) { item -> "Подсказка" }

        )
    )
}

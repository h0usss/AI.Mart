package com.h0uss.aimart.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.ProductStatus
import com.h0uss.aimart.data.model.UserProductCardData
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.NotificationsCount
import com.h0uss.aimart.ui.assets.card.MyProductCard
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.mediumStyle
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.main.MyProductsEvent
import com.h0uss.aimart.ui.viewModel.main.MyProductsState

@Composable
fun MyProductsScreen(
    modifier: Modifier = Modifier,
    state: MyProductsState = MyProductsState(),
    onEvent: (MyProductsEvent) -> Unit = {},
) {
    val tabs = listOf("Ожидание", "Активные", "Архив")
    val pagerState = rememberPagerState { tabs.size }
    var selectedTabIndex by remember{ mutableIntStateOf(0) }

    val productsByStatus: Map<ProductStatus, List<UserProductCardData>> = remember(state.products) {
        state.products.groupBy { it.status }
    }

    val waitingList = productsByStatus.getOrElse(ProductStatus.IN_MODERATING_PROCESS) { emptyList() } +
            productsByStatus.getOrElse(ProductStatus.MODERATING_FAILED) { emptyList() }
    val activeList = productsByStatus.getOrElse(ProductStatus.ACTIVE) { emptyList() }
    val archiveList = productsByStatus.getOrElse(ProductStatus.ARCHIVE) { emptyList() }

    val productsFiltered = listOf(waitingList, activeList, archiveList)


    val listStates = remember {
        List(tabs.size) { LazyListState() }
    }
    val currentListState = listStates[pagerState.currentPage]
    val buttonVisible by remember {
        derivedStateOf {
            currentListState.firstVisibleItemScrollOffset <= 100 && currentListState.firstVisibleItemIndex == 0
        }
    }


    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    Column(
        modifier = modifier
            .background(White)
            .padding(start = 16.dp, top = 65.dp, end = 16.dp)
            .fillMaxSize()
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Объявления",
            style = semiboldStyle,
            color = Black80,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .padding(top = 18.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Black10,
                    shape = RoundedCornerShape(30)
                )
                .padding(16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Баланс",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black80
            )
            Text(
                text = "$${state.balance}",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black80
            )
        }

        SecondaryTabRow (
            modifier = Modifier.padding(top = 22.dp),
            selectedTabIndex = selectedTabIndex,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false),
                    color = Teal,
                    height = 1.dp
                )
            },
            containerColor = White,
        ){
            tabs.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(bottom = 5.dp)
                        .clickable {
                            selectedTabIndex = index
                        }
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = item,
                        style = mediumStyle,
                        fontSize = 14.sp,
                        color =
                            if (index == selectedTabIndex) Teal
                            else Black80
                    )
                    if (productsFiltered[index].isNotEmpty())
                        NotificationsCount(
                            count = productsFiltered[index].size,
                            isActive = index == selectedTabIndex
                        )
                }
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ,
            state = pagerState,

            ) { index ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listStates[index]
            ) {
                if (productsFiltered[index].isEmpty()){
                    item {
                        Column(
                            modifier = Modifier.padding(top = 60.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.no_products),
                                contentDescription = "No products"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 14.dp),
                                text = "Здесь пока ничего нет",
                                style = semiboldStyle,
                                fontSize = 18.sp,
                                color = Black100,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else if (index == 0) {
                    val failed = productsFiltered[index].filter { it.status == ProductStatus.MODERATING_FAILED }
                    val inModeration = productsFiltered[index].filter { it.status == ProductStatus.IN_MODERATING_PROCESS }

                    if (failed.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(top = 18.dp),
                                text = "Не прошли модерацию",
                                style = regularStyle,
                                fontSize = 14.sp,
                                color = Black100
                            )
                        }
                        items(failed) { item ->
                            MyProductCard(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .clickable{
                                        onEvent(MyProductsEvent.ProductClick(item.id))
                                    }
                                ,
                                product = item,
                            )
                        }
                    }

                    if (inModeration.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(top = 18.dp),
                                text = "На модерации",
                                style = regularStyle,
                                fontSize = 14.sp,
                                color = Black100
                            )
                        }
                        items(inModeration) { item ->
                            MyProductCard(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .clickable{
                                        onEvent(MyProductsEvent.ProductClick(item.id))
                                    }
                                ,
                                product = item)
                        }
                    }
                }
                else {
                    items(productsFiltered[index]) { item ->
                        MyProductCard(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .clickable{
                                    onEvent(MyProductsEvent.ProductClick(item.id))
                                }
                            ,
                            product = item
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = buttonVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(bottom = 8.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    text = "Разместить объявление",
                    onClick = {
                        onEvent(MyProductsEvent.NewProductClick)
                    }
                )
            }
        }

    }
}


@Preview
@Composable
private fun Preview() {
    MyProductsScreen(
        state = MyProductsState(
            balance = "1,000",
            products = listOf(
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.MODERATING_FAILED,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.MODERATING_FAILED,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.IN_MODERATING_PROCESS,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.ACTIVE,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
                UserProductCardData(
                    id = 1L,
                    name = "Товар",
                    price = 100.01f,
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                    status = ProductStatus.MODERATING_FAILED,
                    description = "Описание товара описание товара описание товара описание товара описание товара описание товара"
                ),
            )
        )
    )
}
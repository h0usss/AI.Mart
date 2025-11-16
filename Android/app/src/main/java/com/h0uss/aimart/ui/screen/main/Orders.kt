package com.h0uss.aimart.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.model.ProductCardData
import com.h0uss.aimart.data.model.StatusData
import com.h0uss.aimart.ui.assets.Status
import com.h0uss.aimart.ui.assets.card.ProductCard
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun Orders(
    modifier: Modifier = Modifier,
    products: List<ProductCardData>,
    tags: List<StatusData>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 16.dp)
    ){
        item{
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 63.dp)
                ,
                text = "Заказы",
                style = semiboldStyle,
                fontSize = 16.sp,
                color = Black100,
                textAlign = TextAlign.Center,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                tags.forEach { item ->
                    Status(
                        statusData = item,
                    )
                }
            }
        }

        val chunkedProducts = products.chunked(2)

        items(chunkedProducts) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
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

@Preview
@Composable
private fun Preview_Full() {
    Orders(
        products = List(21) { item ->
            ProductCardData(
                author = "Бильбо",
                name = "AI Кольцо всевластия",
                price = 0.99f,
                imageId = R.drawable.background,
                status = OrderStatus.COMPLETE
            )
        },
        tags = listOf(
            StatusData(
                status = OrderStatus.DEBATE,
                count = 0,
                isTag = true
            ),
            StatusData(
                status = OrderStatus.WAITING,
                count = 1,
                isTag = true
            ),
            StatusData(
                status = OrderStatus.IN_WORK,
                count = 10,
                isTag = true
            ),
            StatusData(
                status = OrderStatus.COMPLETE,
                count = 100,
                isTag = true
            ),
        )
    )
}

//@Preview(showSystemUi = true)
//@Composable
//private fun Preview_Empty() {
//    Orders(
//        products = listOf(),
//        tags = listOf(
//            StatusData(
//                status = OrderStatus.DEBATE,
//                count = 0,
//                isTag = true
//            ),
//            StatusData(
//                status = OrderStatus.WAITING,
//                count = 1,
//                isTag = true
//            ),
//            StatusData(
//                status = OrderStatus.IN_WORK,
//                count = 10,
//                isTag = true
//            ),
//            StatusData(
//                status = OrderStatus.COMPLETE,
//                count = 100,
//                isTag = true
//            ),
//        )
//    )
//}

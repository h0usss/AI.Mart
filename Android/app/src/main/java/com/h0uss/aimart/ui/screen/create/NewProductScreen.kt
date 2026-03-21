package com.h0uss.aimart.ui.screen.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.FormField
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.assets.TextFieldNewProduct
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.create.NewProductEvent
import com.h0uss.aimart.ui.viewModel.create.NewProductState

@Composable
fun NewProductScreen(
    modifier: Modifier = Modifier,
    state: NewProductState = NewProductState(),
    onEvent: (NewProductEvent) -> Unit = {},
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onEvent(NewProductEvent.AddImages(uris.map { it.toString() }))
        }
    }

    LaunchedEffect(state.images) {
        onEvent(NewProductEvent.ClearError(FormField.IMAGE))
    }
    LaunchedEffect(state.price.text) {
        onEvent(NewProductEvent.ClearError(FormField.PRICE))
    }
    LaunchedEffect(state.name.text) {
        onEvent(NewProductEvent.ClearError(FormField.NAME))
    }
    LaunchedEffect(state.desc.text) {
        onEvent(NewProductEvent.ClearError(FormField.DESCRIPTION))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 16.dp, top = 66.dp, end = 16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Back",
                )
                Text(
                    modifier = Modifier,
                    text = "Новое объявление",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black80
                )
                Text(
                    text = "",
                    style = semiboldStyle,
                    fontSize = 16.sp,
                    color = Black80
                )
            }

            if (state.images.isEmpty())
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Black5)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        modifier = Modifier
                            .clickable {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                        painter = painterResource(R.drawable.no_image),
                        contentDescription = "No image",
                    )
                }
            else
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items( state.images ){ imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(240.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (state.images.size < 5)
                        item{
                            Image(
                                modifier = Modifier
                                    .clickable {
                                        launcher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    },
                                painter = painterResource(R.drawable.no_image),
                                contentDescription = "No image",
                            )
                        }
                }
            if (state.imageError != null) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource( R.drawable.error_status ),
                        contentDescription = "Error"
                    )
                    Text(
                        text = " ${state.imageError}",
                        fontSize = 12.sp,
                        style = regularStyle,
                        color = ErrorText,
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 14.dp, bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Цена ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                fontSize = 14.sp,
                color = Black80
            )
            TextFieldNewProduct(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "Введите цену",
                state = state.price,
                errorMessage = state.priceError ?: "",
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Название ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                fontSize = 14.sp,
                color = Black80,
            )
            TextField(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "Введите название товара",
                state = state.name,
                errorMessage = state.nameError ?: "",
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Описание ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                fontSize = 14.sp,
                color = Black80
            )
            TextField(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "Опишите товар, его характеристики, условия выполнения и т.д.",
                state = state.desc,
                errorMessage = state.descError ?: "",
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
            ,
            text = "Разместить объявление",
            onClick = {
                onEvent(NewProductEvent.AddProduct)
            }
        )
    }
}


@Preview
@Composable
private fun Preview_v1() {
    NewProductScreen(
        state = NewProductState(
            images = listOf()
        )
    )
}


@Preview
@Composable
private fun Preview_v2() {
    NewProductScreen(
        state = NewProductState(
            images = listOf(
                "android.resource://com.h0uss.aimart/${R.drawable.background}",
                "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",)
        )
    )
}


@Preview
@Composable
private fun Preview_v3() {
    NewProductScreen(
        state = NewProductState(
            images = listOf()
        )
    )
}

@Preview
@Composable
private fun Preview_v4() {
    NewProductScreen(
        state = NewProductState(
            images = listOf(),
            imageError = "123"
        )
    )
}
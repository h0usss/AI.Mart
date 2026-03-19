package com.h0uss.aimart.ui.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.assets.RecentSearchTerm
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.theme.Black45
import com.h0uss.aimart.ui.theme.Blue
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.viewModel.search.SearchEvent
import com.h0uss.aimart.ui.viewModel.search.SearchState

@SuppressLint("RememberInComposition")
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState = SearchState(),
    onEvent: (SearchEvent) -> Unit = {},
) {
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 16.dp, top = 11.dp, end = 16.dp)
            .systemBarsPadding()
    ){
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                modifier = Modifier
                    .weight(.1f)
                    .focusRequester(focusRequester)
                ,
                isFocus = true,
                isFill = true,
                placeHolder = "Поиск",
                state = state.searchState,
                rightImageId = R.drawable.close,
                leftImageId = R.drawable.loupe,
                onClickRightImage = {
                    onEvent(SearchEvent.ClearSearchClick)
                    focusManager.clearFocus()
                },
                onClickEnter = {
                    onEvent(SearchEvent.SearchRequest(state.searchState.text.toString()))
                }
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable{
                        onEvent(SearchEvent.CancelClick)
                    }
                ,
                text = "Отмена",
                style = regularStyle,
                fontSize = 14.sp,
                color = Blue
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = "История",
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black45
                    )
                    Text(
                        modifier = Modifier
                            .clickable{
                                onEvent(SearchEvent.ClearHintsClick)
                            },
                        text = "Очистить",
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Blue
                    )
                }
            }
            items(state.lastSearchList){ item ->
                RecentSearchTerm(
                    modifier = Modifier.padding(top = 8.dp),
                    name = item,
                    onClick = {
                        onEvent(SearchEvent.SearchRequest(item))
                    },
                    onXClick = {
                        onEvent(SearchEvent.DeleteHintClick(item))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SearchScreen(
        state = SearchState(
            lastSearchList = listOf(
                "Подсказка", "Подарок", "Фотография"
            )
        )
    )
}
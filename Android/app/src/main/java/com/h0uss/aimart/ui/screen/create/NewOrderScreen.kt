package com.h0uss.aimart.ui.screen.create

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.create.NewOrderEvent
import com.h0uss.aimart.ui.viewModel.create.NewOrderState
import com.h0uss.aimart.util.DateValidatorNoPast
import com.h0uss.aimart.util.inputTransformation.dateInputTransformation
import com.h0uss.aimart.util.inputTransformation.floatInputTransformation
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewOrderScreen(
    modifier: Modifier = Modifier,
    state: NewOrderState = NewOrderState(),
    sellerId: Long,
    productId: Long,
    onEvent: (NewOrderEvent) -> Unit = {},
) {

    val datePickerState = rememberDatePickerState(
        selectableDates = DateValidatorNoPast
    )
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                onEvent(NewOrderEvent.Exit)
            }
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(16.dp)
                .clickable(enabled = false) {}
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                text = "Техническое задание",
                style = semiboldStyle,
                color = Black80,
                fontSize = 18.sp,
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Описание задачи ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                color = Black80,
                fontSize = 14.sp,
            )
            TextField(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "Подробно опишите задачу, на что обратить внимание",
                isBigTextField = true,
                state = state.desc
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Какой у вас дедлайн? ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                color = Black80,
                fontSize = 14.sp,
            )
            TextField(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "DD.MM.YYYY",
                state = state.date,
                inputTransformation = dateInputTransformation(),
                rightImageId = R.drawable.calendar,
                onClickRightImage = {
                    showDatePicker = true
                }
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Какой у вас бюджет? ")
                    withStyle(
                        style = SpanStyle(color = ErrorText)
                    ) {
                        append("*")
                    }
                },
                style = semiboldStyle,
                color = Black80,
                fontSize = 14.sp,
            )
            TextField(
                modifier = Modifier.padding(bottom = 16.dp),
                placeHolder = "10\$",
                inputTransformation = floatInputTransformation(),
                state = state.price
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                text = "Отправить",
                isGray = state.desc.text.isEmpty() || state.date.text.isEmpty() || state.price.text.isEmpty(),
                onClick = {
                    if (state.desc.text.isNotEmpty() && state.date.text.isNotEmpty() && state.price.text.isNotEmpty()) {
                        onEvent(
                            NewOrderEvent.Send(
                                sellerId = sellerId,
                                productId = productId
                            )
                        )
                    }
                }
            )
        }

        Image(
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable { onEvent(NewOrderEvent.Exit) },
            painter = painterResource(R.drawable.close_portfolio),
            contentDescription = "Close order"
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val instant = Instant.ofEpochMilli(selectedMillis)
                            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            val formattedDate = formatter.format(localDate)
                            onEvent(NewOrderEvent.DateSelected(formattedDate.toString()))
                            Log.e("AIMARTR", formattedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    NewOrderScreen(
        sellerId = -1L,
        productId = -1L,
    )
}
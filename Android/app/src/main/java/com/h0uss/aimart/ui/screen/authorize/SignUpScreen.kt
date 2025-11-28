package com.h0uss.aimart.ui.screen.authorize

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.emun.FormField
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.TextField
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black45
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.agreementAnnotatedString
import com.h0uss.aimart.ui.theme.haveAccountAnnotatedString
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.authorize.SignUpEvent
import com.h0uss.aimart.ui.viewModel.authorize.SignUpState
import com.h0uss.aimart.util.DateValidator
import com.h0uss.aimart.util.inputTransformation.dateInputTransformation
import com.h0uss.aimart.util.inputTransformation.emailInputTransformation
import com.h0uss.aimart.util.inputTransformation.nameInputTransformation
import com.h0uss.aimart.util.inputTransformation.passwordInputTransformation
import com.h0uss.aimart.util.outputTransformation.passwordOutputTransformation
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: SignUpState = SignUpState(),
    onEvent: (SignUpEvent) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = DateValidator
    )
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.nameState.text) {
        onEvent(SignUpEvent.ClearError(FormField.NAME))
    }
    LaunchedEffect(state.emailState.text) {
        onEvent(SignUpEvent.ClearError(FormField.EMAIL))
    }
    LaunchedEffect(state.passwordState.text) {
        onEvent(SignUpEvent.ClearError(FormField.PASSWORD))
    }
    LaunchedEffect(state.dateState.text) {
        onEvent(SignUpEvent.ClearError(FormField.DATE))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 24.dp, top = 72.dp, end = 24.dp )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "AI Mart",
            color = Black100,
            fontSize = 24.sp,
            style = semiboldStyle,
        )
        Text(
            modifier = Modifier
                .padding(top = 48.dp)
            ,
            text = "Регистрация",
            color = Black100,
            fontSize = 18.sp,
            style = semiboldStyle,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
            ,
            text = "Имя",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "Ваше имя",
            inputTransformation = nameInputTransformation(),
            state = state.nameState,
            errorMessage = state.nameError ?: "",
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "E-mail",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "example@gmail.com",
            inputTransformation = emailInputTransformation(),
            state = state.emailState,
            errorMessage = state.emailError ?: "",
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "Дата рождения",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "DD.MM.YYYY",
            state = state.dateState,
            inputTransformation = dateInputTransformation(),
            errorMessage = state.dateError ?: "",
            rightImageId = R.drawable.calendar,
            onClickRightImage = {
                showDatePicker = true
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ,
            text = "Пароль",
            color = Black80,
            fontSize = 14.sp,
            style = semiboldStyle,
        )
        TextField(
            modifier = Modifier
                .padding(top = 6.dp)
            ,
            placeHolder = "Пароль от 8 символов",
            inputTransformation = passwordInputTransformation(),
            outputTransformation = passwordOutputTransformation(),
            state = state.passwordState,
            errorMessage = state.passwordError ?: "",
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            text = "Продолжить",
            rightImageId = R.drawable.arrow,
            onClick = {
                onEvent(SignUpEvent.SignUpClicked)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            HorizontalDivider(
                modifier = Modifier
                    .width(143.dp)
                ,
                color = Black10
            )
            Text(
                text = "или",
                color = Black45,
                fontSize = 14.sp,
                style = regularStyle,
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(143.dp)
                ,
                color = Black10
            )
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            text = "Использовать",
            isGray = true,
            onClick = {
                onEvent(SignUpEvent.GoogleSignUpClicked)
            },
            leftImageId = R.drawable.google
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text =  buildAnnotatedString {
                agreementAnnotatedString(
                    isLogin = false,
                    onClickAgreement = {
                        onEvent(SignUpEvent.AgreementClicked)
                    },
                    onClickPrivacyPolicy = {
                        onEvent(SignUpEvent.PrivacyPolicyClicked)}
                )
            },
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text =  buildAnnotatedString {
                haveAccountAnnotatedString(
                    onClickLogin = {
                        onEvent(SignUpEvent.LoginClicked)
                    }
                )
            }
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
                            onEvent(SignUpEvent.DateSelected(formattedDate.toString()))
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
@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    SignUpScreen()
}

package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black3
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.ErrorBG
import com.h0uss.aimart.ui.theme.ErrorDarkBG
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.Inter
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun TextFieldNewProduct(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    placeHolder: String,
    radiusDp: Dp = 6.dp,
    inputTransformation: InputTransformation = InputTransformation {},
    outputTransformation: OutputTransformation = OutputTransformation {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
    state: TextFieldState = remember { TextFieldState("") },
    onClickEnter: () -> Unit = {},
) {

    val focusManager = LocalFocusManager.current
    val keyboardActions = remember(onClickEnter) {
        KeyboardActionHandler({
            onClickEnter()
            focusManager.clearFocus()
        }
        )
    }

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier,
            state = state,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = keyboardActions,
            outputTransformation = outputTransformation,
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = inputTransformation,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                color = Black80
            ),
            cursorBrush = SolidColor(Black80),
            decorator = { innerTextField ->
                Row(
                    modifier = Modifier
                        .heightIn(min = 40.dp)
                        .clip(RoundedCornerShape(radiusDp))
                        .border(
                            color =
                                if (!errorMessage.isEmpty()) ErrorText
                                else Black10,
                            width = 1.dp,
                            shape = RoundedCornerShape(radiusDp)
                        )
                        .background(
                            color =
                                if (!errorMessage.isEmpty()) ErrorBG
                                else White
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = radiusDp,
                                        bottomStart = radiusDp,
                                    )
                                )
                                .padding(end = 16.dp)
                                .background(
                                    if (!errorMessage.isEmpty()) ErrorDarkBG
                                    else Black3
                                )
                                .padding(horizontal = 16.dp, vertical = 11.dp),
                            text = "от"
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(start = 31.dp),
                            thickness = 1.dp,
                            color = Black10
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if (state.text.isEmpty())
                                Text(
                                    text = placeHolder,
                                    fontSize = 14.sp,
                                    style = regularStyle,
                                    color = Black50,
                                )
                            innerTextField()
                        }
                    }

                    Box(
                        contentAlignment = Alignment.CenterStart
                    ){
                        Text(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topEnd = radiusDp,
                                        bottomEnd = radiusDp,
                                    )
                                )
                                .padding(start = 16.dp)
                                .background(
                                    if (!errorMessage.isEmpty()) ErrorDarkBG
                                    else Black3
                                )
                                .padding(horizontal = 16.dp, vertical = 11.dp),
                            text = "$"
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(start = 16.dp)
                            ,
                            thickness = 1.dp,
                            color = Black10
                        )
                    }
                }

            }
        )

        if (!errorMessage.isEmpty()) {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.error_status),
                    contentDescription = "Error"
                )
                Text(
                    text = " $errorMessage",
                    fontSize = 12.sp,
                    style = regularStyle,
                    color = ErrorText,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNecessarily() {
    Column() {
        TextFieldNewProduct(
            placeHolder = "Пароль",
            errorMessage = "aAlax",
        )
        TextFieldNewProduct(
            placeHolder = "Пароль",
        )
        TextFieldNewProduct(
            placeHolder = "Пароль",
        )
        TextFieldNewProduct(
            placeHolder = "Пароль",
        )
        TextFieldNewProduct(
            placeHolder = "Пароль",
            state = remember { TextFieldState("dfasjhdlhas jd sj fdkjlshd flksdfs dsdh sdjk lasldk jhfaslkdjfhlskahj dhfk jsdhakjshdf slkjh fkjsd hlsdkjhf lskjdh fljksda hldjhf ljks dhahf ljkash fdasjhkl f") }
        )
    }
}
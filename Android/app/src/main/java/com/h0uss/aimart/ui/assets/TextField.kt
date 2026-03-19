package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.ErrorBG
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.Inter
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    isFocus: Boolean = false,
    isFill: Boolean = false,
    isBigTextField: Boolean = false,
    placeHolder: String,
    radiusPercent: Int = 20,
    inputTransformation: InputTransformation = InputTransformation{},
    outputTransformation: OutputTransformation = OutputTransformation{},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
    state: TextFieldState = remember { TextFieldState("") },
    rightImageId: Int = -1,
    leftImageId: Int = -1,
    onClickRightImage: () -> Unit = {},
    onClickLeftImage: () -> Unit = {},
    onClickEnter: () -> Unit = {},
){

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
    ){
        BasicTextField(
            modifier = Modifier,
            state = state,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = keyboardActions,
            outputTransformation = outputTransformation,
            lineLimits =
                if (isBigTextField) TextFieldLineLimits.Default
                else TextFieldLineLimits.SingleLine,
            inputTransformation = inputTransformation,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                color = Black80
            ),
            cursorBrush = SolidColor(Black80),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp)
                        .clip(RoundedCornerShape(radiusPercent))
                        .border(
                            color =
                                if (!errorMessage.isEmpty()) ErrorText
                                else if (isFocus) Black80
                                else Black10,
                            width = 1.dp,
                            shape = RoundedCornerShape(radiusPercent)
                        )
                        .background(
                            color =
                                if (!errorMessage.isEmpty()) ErrorBG
                                else if (isFill) Black5
                                else White
                        )
                        .padding(
                            start =
                                if (leftImageId == -1) 16.dp
                                else 8.dp,
                            top = 8.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        if (leftImageId != -1)
                            Image(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        onClickLeftImage()
                                    },
                                painter = painterResource(leftImageId),
                                contentDescription = "Left image"
                            )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ){
                            if (state.text.isEmpty())
                                Text(
                                    text = placeHolder,
                                    fontSize = 14.sp,
                                    style = regularStyle,
                                    color = Black50,
                                )
                            innerTextField()
                        }

                        if (rightImageId != -1)
                            Image(
                                modifier = Modifier.clickable{
                                    onClickRightImage()
                                },
                                painter = painterResource(rightImageId),
                                contentDescription = "RightImage"
                            )
                    }
                }
            }
        )

        if (!errorMessage.isEmpty()) {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource( R.drawable.error_status ),
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
    Column(){
        TextField(
            placeHolder = "Пароль",
            errorMessage = "aAlax",
        )
        TextField(
            placeHolder = "Пароль",
            isFocus = true
        )
        TextField(
            placeHolder = "Пароль",
            rightImageId = R.drawable.calendar
        )
        TextField(
            placeHolder = "Пароль",
            leftImageId = R.drawable.loupe
        )
        TextField(
            placeHolder = "Пароль",
            isBigTextField = true,
            state = remember{ TextFieldState("dfasjhdlhas jd sj fdkjlshd flksdfs dsdh sdjk lasldk jhfaslkdjfhlskahj dhfk jsdhakjshdf slkjh fkjsd hlsdkjhf lskjdh fljksda hldjhf ljks dhahf ljkash fdasjhkl f") }
        )
    }
}
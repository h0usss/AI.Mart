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
import androidx.compose.foundation.text.input.KeyboardActionHandler
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
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Inter
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun SendMessageField(
    modifier: Modifier = Modifier,
    placeHolder: String = "Send a message…",
    state: TextFieldState = remember { TextFieldState("") },
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
            onKeyboardAction = keyboardActions,
            lineLimits = TextFieldLineLimits.SingleLine,
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
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            color = Black10,
                            width = 1.dp,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(
                            color = White
                        )
                        .padding(
                            start = 16.dp,
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

                        if (!state.text.isEmpty())
                            Image(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable{
                                        onClickEnter()
                                    }
                                ,
                                painter = painterResource(R.drawable.send),
                                contentDescription = "RightImage"
                            )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewNecessarily() {
    Column(){
        SendMessageField(
        )
        SendMessageField(
            state = remember{ TextFieldState("dfasjhdlhas jd sj fdkjlshd flksdfs dsdh sdjk lasldk jhfaslkdjfhlskahj dhfk jsdhakjshdf slkjh fkjsd hlsdkjhf lskjdh fljksda hldjhf ljks dhahf ljkash fdasjhkl f") }
        )
    }
}
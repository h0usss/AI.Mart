package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black10
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
    inputTransformation: InputTransformation = InputTransformation{},
    outputTransformation: OutputTransformation = OutputTransformation{},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    state: TextFieldState = remember { TextFieldState("") },
    placeHolder: String,
)
{
    Column(

    ){
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth(),
            state = state,
            keyboardOptions = keyboardOptions,
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
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(20))
                        .border(
                            color =
                                if (!errorMessage.isEmpty()) ErrorText
                                else if (isFocus) Black80
                                else Black10,
                            width = 1.dp,
                            shape = RoundedCornerShape(20)
                        )
                        .background(
                            color =
                                if (!errorMessage.isEmpty()) ErrorBG
                                else White
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
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
fun PreviewNecessarily() {
    Column(){
        TextField(
            placeHolder = "Пароль",
            errorMessage = "aAlax"
        )
        TextField(
            placeHolder = "Пароль",
            isFocus = true
        )

    }
}
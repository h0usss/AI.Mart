package com.h0uss.aimart.ui.assets

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
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.BlackProfileAdd
import com.h0uss.aimart.ui.theme.Inter
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle

@Composable
fun AddTag(
    modifier: Modifier = Modifier,
    placeHolder: String,
    inputTransformation: InputTransformation = InputTransformation{},
    outputTransformation: OutputTransformation = OutputTransformation{},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    state: TextFieldState = TextFieldState(""),
    onClickAdd: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ){
        BasicTextField(
            modifier = Modifier,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp)
                        .clip(RoundedCornerShape(10))
                        .border(
                            color = Black10,
                            width = 1.dp,
                            shape = RoundedCornerShape(10)
                        )
                        .background(
                            color = White
                        )
                        .padding(
                            start = 16.dp,
                            top = 8.dp,
                            end = 4.dp,
                            bottom = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            Box {
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
                            modifier = Modifier
                                .clip(RoundedCornerShape(20))
                                .background(BlackProfileAdd)
                                .clickable{
                                    onClickAdd()
                                }
                            ,
                        ){
                            Text(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 14.dp),
                                text = "+ добавить",
                                style = semiboldStyle,
                                fontSize = 14.sp,
                                color = White,
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AddTag(
        placeHolder = "7 - максимально"
    )
}
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black5
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.regularStyle

@Composable
fun SearchTextFieldButton(
    modifier: Modifier = Modifier,
    radiusPercent: Int = 20,
    placeHolder: String,
    value: String = "",
    rightImageId: Int = -1,
    leftImageId: Int = -1,
    onClickRightImage: () -> Unit = {},
    onClickLeftImage: () -> Unit = {},
){
    Column(
        modifier = modifier
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clip(RoundedCornerShape(radiusPercent))
                .border(
                    color = Black10,
                    width = 1.dp,
                    shape = RoundedCornerShape(radiusPercent)
                )
                .background(
                    color = Black5
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
                            }
                        ,
                        painter = painterResource(leftImageId),
                        contentDescription = "Left image"
                    )

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (value.isEmpty())
                        Text(
                            text = placeHolder,
                            fontSize = 14.sp,
                            style = regularStyle,
                            color = Black50,
                        )
                    else
                        Text(
                            text = value,
                            fontSize = 14.sp,
                            style = regularStyle,
                            color = Black80,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,

                        )
                }


                if (rightImageId != -1)
                    Image(
                        modifier = Modifier
                            .clickable{
                                onClickRightImage()
                            }
                        ,
                        painter = painterResource(rightImageId),
                        contentDescription = "RightImage"
                    )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SearchTextFieldButton(
        leftImageId = R.drawable.loupe,
        placeHolder = "Поиск",
        value = "adsfjasdglkhsa djgfsdhjfhsdl fjasldkjfsaldk fjsld; fsldjk f;sa",
        rightImageId = R.drawable.close
    )
}
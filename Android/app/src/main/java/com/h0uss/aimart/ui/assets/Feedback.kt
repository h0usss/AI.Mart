package com.h0uss.aimart.ui.assets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.FeedbackData
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.util.customFormatter
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Feedback(
    modifier: Modifier = Modifier,
    feedbackData: FeedbackData
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Row{
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = feedbackData.user.imageUrl,
                contentDescription =  feedbackData.user.name,
            )
            Column(
                modifier = Modifier.padding(start = 18.dp, top = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = feedbackData.user.name,
                        style = semiboldStyle,
                        fontSize = 18.sp,
                        color = Black80
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = feedbackData.date.format(customFormatter),
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black50
                    )
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        for (i in 1..feedbackData.starCount)
                            Image(
                                painter = painterResource(R.drawable.star_yellow_feedback),
                                contentDescription = "Star yellow"
                            )
                        for (i in 1..5 - feedbackData.starCount)
                            Image(
                                painter = painterResource(R.drawable.star_black_feedback),
                                contentDescription = "Star black"
                            )
                    }
                }
                Text(
                    text = feedbackData.text,
                    style = regularStyle,
                    fontSize = 14.sp,
                    color = Black80
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding( top = 12.dp ),
            thickness = 1.dp,
            color = Black10
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Feedback(
            feedbackData = FeedbackData(
                user = UserData(
                    name = "Пип",
                    nick = "@df",
                    imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                    rate = 5.0f,
                ),
                text = "Гавна гавной",
                starCount = 1,
                date = LocalDateTime.of(2022, 12, 11, 11, 11)
            )
        )
        Feedback(
            feedbackData = FeedbackData(
                user = UserData(
                    name = "Пипиня",
                    nick = "@df",
                    imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                    rate = 5.0f,
                ),
                text = "Вакцин хийлгэсэн хүмүүс нь илүү үхсэн байгаа шүү дээ.өвчин дээр нь нэмээд вакцин... бүгд хүн шулмууд",
                starCount = 2,
                date = LocalDateTime.of(2022, 12, 11, 11, 11)
            )
        )
        Feedback(
            feedbackData = FeedbackData(
                user = UserData(
                    name = "Пипиня",
                    nick = "@df",
                    imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                    rate = 5.0f,
                ),
                text = "2 л бол 2 шүү дээ, зурхайгаа дагаж амарцгаасан нь дээр шүү, дараа нь яах ч юм билээ, араа бодож зурх",
                starCount = 3,
                date = LocalDateTime.of(2022, 12, 11, 11, 11)
            )
        )
        Feedback(
            feedbackData = FeedbackData(
                user = UserData(
                    name = "Пипиня",
                    nick = "@df",
                    imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                    rate = 5.0f,
                ),
                text = "2 л бол 2 шүү дээ, зурхайгаа дагаж амарцгаасан нь дээр шүү, дараа нь яах ч юм билээ, араа бодож зурх",
                starCount = 4,
                date = LocalDateTime.of(2022, 12, 11, 11, 11)
            )
        )
        Feedback(
            feedbackData = FeedbackData(
                user = UserData(
                    name = "Пипиня",
                    nick = "@df",
                    imageUrl = "android.resource://com.h0uss.aimart/${R.drawable.seller}",
                    rate = 5.0f,
                ),
                text = "Чупапиназо",
                starCount = 5,
                date = LocalDateTime.of(2022, 12, 11, 11, 11)
            )
        )
    }

}
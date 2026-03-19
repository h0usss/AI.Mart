package com.h0uss.aimart.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.assets.Dropdown
import com.h0uss.aimart.ui.assets.StatisticBox
import com.h0uss.aimart.ui.assets.TopBar
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForOtherEvent
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForOtherState

@Composable
fun UserProfileForOtherScreen(
    modifier: Modifier = Modifier,
    state: UserProfileForOtherState = UserProfileForOtherState(),
    onEvent: (UserProfileForOtherEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 16.dp, end = 16.dp)
            .systemBarsPadding()
        ,
    ){
        Box{
            TopBar(
                onBackClick = {
                    onEvent(UserProfileForOtherEvent.BackClick)
                },
                onAdditionalClick = {
                    onEvent(UserProfileForOtherEvent.ShowAdditionalMenu)
                },
            )
            Dropdown(
                isAdditional = true,
                isVisible = state.isShowAdditional,
                offset = DpOffset(
                    x = (-25).dp,
                    y = 0.dp
                ),
                onItemClick = { id ->
                    onEvent(UserProfileForOtherEvent.AdditionalItemClick(id))
                },
                onDismissRequest = {
                    onEvent(UserProfileForOtherEvent.DismissAdditionalMenu)
                }
            )
        }
        Box(
            modifier = Modifier.padding(top = 14.dp),
            contentAlignment = Alignment.TopEnd
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(White)
                    .border(
                        width = 1.dp,
                        color = Black10,
                        shape = RoundedCornerShape(15.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(96.dp)
                        .clip(CircleShape),
                    painter = painterResource(state.user.imageId),
                    contentDescription = state.user.nick,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = state.user.name,
                    style = semiboldStyle,
                    fontSize = 18.sp,
                    color = Black80
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp, bottom = 18.dp),
                    text = state.user.nick,
                    style = regularStyle,
                    fontSize = 16.sp,
                    color = Black50
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatisticBox(
                modifier = Modifier.weight(1f),
                imageId = R.drawable.star,
                num = state.user.rate.toString(),
                name = "Рейтинг"
            )
            StatisticBox(
                modifier = Modifier.weight(1f),
                imageId = R.drawable.handshake,
                num = state.countBuy.toString(),
                name = "Сделки"
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    UserProfileForOtherScreen(
        state = UserProfileForOtherState(
            user = UserData(
                name = "Алиса",
                nick = "@al1s",
                imageId = R.drawable.avatar_0,
                rate = 5.0f,
            )
        )
    )
}
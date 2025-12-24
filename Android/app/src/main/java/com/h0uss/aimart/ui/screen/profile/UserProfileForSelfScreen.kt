package com.h0uss.aimart.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.h0uss.aimart.ui.assets.Balance
import com.h0uss.aimart.ui.assets.Button
import com.h0uss.aimart.ui.assets.Dropdown
import com.h0uss.aimart.ui.assets.StatisticBox
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black50
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForSelfEvent
import com.h0uss.aimart.ui.viewModel.profile.UserProfileForSelfState

@Composable
fun UserProfileForSelfScreen(
    modifier: Modifier = Modifier,
    state: UserProfileForSelfState = UserProfileForSelfState(),
    onEvent: (UserProfileForSelfEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 16.dp, end = 16.dp)
            .systemBarsPadding()
        ,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
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
                    modifier = Modifier.padding(top = 2.dp),
                    text = state.user.nick,
                    style = regularStyle,
                    fontSize = 16.sp,
                    color = Black50
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ,
                    text = "Редактировать профиль",
                    isGray = true,
                    onClick = {
                        onEvent(UserProfileForSelfEvent.EditClick(state.user.id))
                    },
                )
            }
            Box(
                contentAlignment = Alignment.TopEnd
            ){
                Image(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 14.dp)
                        .clickable {
                            onEvent(UserProfileForSelfEvent.ShowSettingsMenu)
                        },
                    painter = painterResource(R.drawable.setting),
                    contentDescription = "Setting"
                )
                Dropdown(
                    isSettings = true,
                    isVisible = state.isVisibleSettings,
                    offset = DpOffset(
                        x = (-25).dp,
                        y = 0.dp
                    ),
                    onItemClick = { id ->
                        onEvent(UserProfileForSelfEvent.SettingItemClick(id))
                    },
                    onDismissRequest = {
                        onEvent(UserProfileForSelfEvent.DismissSettingsMenu)
                    }
                )
            }
        }
        Balance(
            balance = state.user.balance,
            onEmptiedClick = {
                onEvent(UserProfileForSelfEvent.EmptiedAccount)
            },
            onReplenishClick = {
                onEvent(UserProfileForSelfEvent.ReplenishAccount)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
    UserProfileForSelfScreen(
        state = UserProfileForSelfState(
            user = UserData(
                name = "Алиса",
                nick = "@al1s",
                imageId = R.drawable.avatar_0,
                rate = 5.0f,
            )
        )
    )
}
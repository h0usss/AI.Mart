package com.h0uss.aimart.ui.screen.chat

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.R
import com.h0uss.aimart.data.enum.OrderStatus
import com.h0uss.aimart.data.model.ChatUserData
import com.h0uss.aimart.data.model.MessageData
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.data.model.UserData
import com.h0uss.aimart.ui.assets.SendMessageField
import com.h0uss.aimart.ui.assets.chat.ChatUserTopBar
import com.h0uss.aimart.ui.assets.chat.MiniTaskBar
import com.h0uss.aimart.ui.assets.chat.MyMessage
import com.h0uss.aimart.ui.assets.chat.OrderEnd
import com.h0uss.aimart.ui.assets.chat.OtherMessage
import com.h0uss.aimart.ui.theme.Black10
import com.h0uss.aimart.ui.theme.Black20
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.chat.ChatUserEvent
import com.h0uss.aimart.ui.viewModel.chat.ChatUserState
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatUserScreen(
    modifier: Modifier = Modifier,
    state: ChatUserState = ChatUserState(),
    onEvent: (ChatUserEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ChatUserTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 55.dp, bottom = 14.dp),
            userData = state.userData,
            onBackClick = {
                onEvent(ChatUserEvent.ToListClick)
            },
            onUserClick = {
                onEvent(ChatUserEvent.UserClick(it))
            },
            onProductClick = {
                onEvent(ChatUserEvent.ProductClick(it))
            }
        )

        if (state.orderData.sellerId == authUserIdLong
            && (state.orderData.status == OrderStatus.IN_WORK
                    || state.orderData.status == OrderStatus.WAIT_PAY)
        )
            MiniTaskBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                orderData = state.orderData,
                onClick = {
                    onEvent(ChatUserEvent.TaskBar)
                }
            )

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    space = 14.dp,
                    alignment = Alignment.Bottom
                ),
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                }
                items(state.messages) { message ->
                    if (message.userId == authUserIdLong) {
                        MyMessage(
                            messageData = message
                        )
                    } else {
                        OtherMessage(
                            messageData = message,
                            onUserClick = { id ->
                                onEvent(ChatUserEvent.UserClick(id))
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                }
            }

            if (state.orderData.buyerId == authUserIdLong
                && state.orderData.status == OrderStatus.WAIT_PAY
            )
                OrderEnd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    orderData = state.orderData,
                    onPayClick = {
                        onEvent(ChatUserEvent.PayClick)
                    },
                    onOpenTicketClick = {

                    },
                    canPay = state.buyer.balance >= state.orderData.price
                )

            SendMessageField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                onClickEnter = { message ->
                    onEvent(ChatUserEvent.SendMessage(message))
                },
                onAttachmentClick = {
                    onEvent(ChatUserEvent.ShowAttachmentSheet)
                },
            )
        }
    }

    if (state.isAttachmentSheetVisible) {
        AttachmentSheet(
            selectedAttachments = state.selectedAttachments,
            isProtectEnabled = state.isProtectEnabled,
            onToggleProtect = { onEvent(ChatUserEvent.ToggleProtect) },
            onToggleAttachment = { uri -> onEvent(ChatUserEvent.ToggleAttachment(uri)) },
            onSend = { onEvent(ChatUserEvent.SendAttachments) },
            onDismiss = { onEvent(ChatUserEvent.HideAttachmentSheet) },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview_Full() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                userId = -1L,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${"android.resource://com.h0uss.aimart/${R.drawable.background}"}" },
                userName = "Pipipupu"
            ),
            messages = List(20) {
                MessageData(
                    text = "1 test my msg",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
                    userId = authUserIdLong,
                )
            }
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, widthDp = 360, heightDp = 1000)
@Composable
private fun Preview_Full_V2() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                userId = -1L,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                userName = "Pipipupu"
            ),
            messages = listOf(
                MessageData(
                    text = "test my msg asd as das dasd as d da sdas da sdas dasd ",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
                    userId = authUserIdLong,
                ),
                MessageData(
                    text = "test otasdasdasd as das as das das das das das das dasher msg",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}",
                    userId = 2,
                ),
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
private fun Preview_Empty() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                userId = -1L,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                userName = "Pipipupu"
            ),
            messages = listOf(),
            orderData = OrderData(
                status = OrderStatus.IN_WORK,
                description = LoremIpsum(100).values.joinToString(" ") { it }
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
private fun Preview_V4() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                userId = -1L,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                userName = "Pipipupu"
            ),
            messages = listOf(
                MessageData(
                    text = "test my msg asd as das dasd as d da sdas da sdas dasd ",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
                    userId = authUserIdLong,
                ),
                MessageData(
                    text = "test otasdasdasd as das as das das das das das das dasher msg",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}",
                    userId = 2,
                ),
            ),
            orderData = OrderData(
                status = OrderStatus.WAIT_PAY,
                description = LoremIpsum(100).values.joinToString(" ") { it },
                price = 100f
            ),
            buyer = UserData(
                balance = 100f
            )
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
private fun Preview_V5() {
    ChatUserScreen(
        state = ChatUserState(
            userData = ChatUserData(
                userId = -1L,
                imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${R.drawable.background}" },
                userName = "Pipipupu"
            ),
            messages = listOf(
                MessageData(
                    text = "test my msg asd as das dasd as d da sdas da sdas dasd ",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.base_avatar}",
                    userId = authUserIdLong,
                ),
                MessageData(
                    text = "test otasdasdasd as das as das das das das das das dasher msg",
                    date = LocalDateTime.now(),
                    avatarUrl = "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}",
                    userId = 2,
                ),
            ),
            orderData = OrderData(
                status = OrderStatus.WAIT_PAY,
                description = LoremIpsum(100).values.joinToString(" ") { it },
                price = 100f
            ),
            buyer = UserData(
                balance = 99f
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentSheet(
    selectedAttachments: List<String>,
    isProtectEnabled: Boolean,
    onToggleProtect: () -> Unit,
    onToggleAttachment: (String) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showPicker = remember { mutableStateOf(true) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        showPicker.value = false
        uris.forEach { uri ->
            onToggleAttachment(uri.toString())
        }
    }

    LaunchedEffect(showPicker.value) {
        if (showPicker.value) {
            photoPickerLauncher.launch("image/*")
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            if (selectedAttachments.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Защитить",
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80,
                    )
                    Switch(
                        checked = isProtectEnabled,
                        onCheckedChange = { onToggleProtect() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Black80,
                            uncheckedThumbColor = White,
                            uncheckedTrackColor = Black10,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                selectedAttachments.forEach { uri ->
                    val isSelected = true
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Teal else Black20,
                                shape = RoundedCornerShape(6.dp),
                            )
                            .background(White)
                            .clickable { onToggleAttachment(uri) },
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(5.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                if (selectedAttachments.size < 10) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, Black20, RoundedCornerShape(6.dp))
                            .background(White)
                            .clickable {
                                showPicker.value = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = "Add",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            }

            if (selectedAttachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Black80)
                        .clickable { onSend() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Отправить (${selectedAttachments.size})",
                        style = semiboldStyle,
                        fontSize = 14.sp,
                        color = White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

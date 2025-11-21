package com.h0uss.aimart.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.DropdownItemData
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.ErrorText
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle

val additionalDropdownItems = listOf(
    DropdownItemData(
        id = "share",
        imageId = R.drawable.share,
        name = "Поделиться",
        textColor = Black80
    ),
    DropdownItemData(
        id = "complaint",
        imageId = R.drawable.complaint,
        name = "Пожаловаться",
        textColor = Black80
    ),
    DropdownItemData(
        id = "block",
        imageId = R.drawable.block_red,
        name = "Заблокировать",
        textColor = ErrorText
    )
)

val settingsDropdownItems = listOf(
    DropdownItemData(
        id = "profile",
        imageId = R.drawable.profile_16,
        name = "Аккаунт",
        textColor = Black80
    ),
    DropdownItemData(
        id = "like",
        imageId = R.drawable.like_16,
        name = "Избранное",
        textColor = Black80
    ),
    DropdownItemData(
        id = "lock",
        imageId = R.drawable.lock,
        name = "Конфиденциальность",
        textColor = Black80
    ),
    DropdownItemData(
        id = "notifications",
        imageId = R.drawable.notifications,
        name = "Уведомления",
        textColor = Black80
    ),
    DropdownItemData(
        id = "globe",
        imageId = R.drawable.globe,
        name = "Язык и валюта",
        textColor = Black80
    ),
    DropdownItemData(
        id = "question",
        imageId = R.drawable.question,
        name = "Помощь",
        textColor = Black80
    )
)

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    dropdownItems: List<DropdownItemData> = listOf(),
    isSettings: Boolean = false,
    isAdditional: Boolean = false,
    onDismissRequest: () -> Unit = {},
    offset: DpOffset = DpOffset.Zero,
    isVisible: Boolean = true,
    onItemClick: (String) -> Unit = {},
) {
    val items =
        if (isSettings) settingsDropdownItems
        else if (isAdditional) additionalDropdownItems
        else dropdownItems

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        DropdownMenu(
            modifier = modifier
                .background(White)
            ,
            expanded = isVisible,
            onDismissRequest = {
                onDismissRequest()
            },
            offset = offset,
            shape = RoundedCornerShape(5.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.height(30.dp),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Image(
                                painter = painterResource(item.imageId),
                                contentDescription = item.name,
                            )
                            Text(
                                text = item.name,
                                style = regularStyle,
                                fontSize = 12.sp,
                                color = item.textColor,
                            )
                        }
                    },
                    onClick = {
                        onItemClick(item.id)
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun Preview() {
    Box(modifier = Modifier.fillMaxSize())
    {
        Dropdown(
            isSettings = true
        )

    }
}
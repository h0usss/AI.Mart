package com.h0uss.aimart.ui.screen.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
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
import com.h0uss.aimart.ui.theme.Black100
import com.h0uss.aimart.ui.theme.Black80
import com.h0uss.aimart.ui.theme.Teal
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.theme.regularStyle
import com.h0uss.aimart.ui.theme.semiboldStyle
import com.h0uss.aimart.ui.viewModel.chat.ChatUserEvent
import com.h0uss.aimart.ui.viewModel.chat.ChatUserState
import com.h0uss.aimart.util.MediaResizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatUserScreen(
    modifier: Modifier = Modifier,
    state: ChatUserState = ChatUserState(),
    onEvent: (ChatUserEvent) -> Unit = {},
) {
    var previewUrl by remember { mutableStateOf<String?>(null) }
    var previewIsVideo by remember { mutableStateOf(false) }
    var previewIsProtected by remember { mutableStateOf(false) }

    val hasProtectedMessages = remember(state.messages) {
        state.messages.any { it.isProtected }
    }
    val activity = LocalContext.current as? Activity
    DisposableEffect(hasProtectedMessages) {
        if (hasProtectedMessages) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        onDispose {
            if (hasProtectedMessages) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

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
                            messageData = message,
                            onImageClick = { url ->
                                previewUrl = url
                                previewIsVideo = false
                                previewIsProtected = message.isProtected
                            },
                            onVideoClick = { url ->
                                previewUrl = url
                                previewIsVideo = true
                                previewIsProtected = message.isProtected
                            },
                        )
                    } else {
                        OtherMessage(
                            messageData = message,
                            onUserClick = { id ->
                                onEvent(ChatUserEvent.UserClick(id))
                            },
                            onImageClick = { url ->
                                previewUrl = url
                                previewIsVideo = false
                                previewIsProtected = message.isProtected
                            },
                            onVideoClick = { url ->
                                previewUrl = url
                                previewIsVideo = true
                                previewIsProtected = message.isProtected
                            },
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

    previewUrl?.let { url ->
        if (previewIsVideo) {
            VideoPreviewDialog(
                url = url,
                isProtected = previewIsProtected,
                onDismiss = { previewUrl = null },
            )
        } else {
            ImagePreviewDialog(
                url = url,
                isProtected = previewIsProtected,
                onDismiss = { previewUrl = null },
            )
        }
    }
}

@Composable
private fun ImagePreviewDialog(
    url: String,
    isProtected: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    DisposableEffect(isProtected) {
        if (isProtected) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        onDispose {
            if (isProtected) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .fillMaxHeight(0.75f)
                    .clipToBounds(),
                contentAlignment = Alignment.Center,
            ) {
                val model = if (isProtected) {
                    ImageRequest.Builder(context)
                        .data(url)
                        .size(150)
                        .build()
                } else {
                    url
                }
                AsyncImage(
                    model = model,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
                if (isProtected) {
                    WatermarkOverlay(modifier = Modifier.fillMaxSize())
                }
            }
            if (!isProtected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(44.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            val saved = MediaResizer.saveToDownloads(context, url)
                            android.widget.Toast.makeText(
                                context,
                                if (saved) "Сохранено в галерею" else "Ошибка сохранения",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                    contentAlignment = Alignment.Center,
                ) {

                    Image(
                        painter = painterResource(R.drawable.download),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
private fun VideoPreviewDialog(
    url: String,
    isProtected: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    DisposableEffect(isProtected) {
        if (isProtected) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        onDispose {
            if (isProtected) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }
    var videoAspectRatio by remember { mutableFloatStateOf(1f) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(videoAspectRatio)
                    .clipToBounds(),
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(url.toUri())
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                if (mp.videoWidth > 0 && mp.videoHeight > 0) {
                                    videoAspectRatio =
                                        mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
                                }
                            }
                            start()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
                if (isProtected) {
                    WatermarkOverlay(modifier = Modifier
                        .matchParentSize()
                        .clipToBounds())
                }
            }
            if (!isProtected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(44.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            val saved = MediaResizer.saveToDownloads(context, url)
                            android.widget.Toast.makeText(
                                context,
                                if (saved) "Сохранено в галерею" else "Ошибка сохранения",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.download),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun WatermarkOverlay(modifier: Modifier = Modifier) {
    val colors = listOf(Black100, Teal, White)
    val watermarkText = "AI.MART"

    BoxWithConstraints(modifier = modifier.clipToBounds()) {
        if (maxWidth <= 0.dp || maxHeight <= 0.dp) return@BoxWithConstraints

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (size.width < 1f || size.height < 1f) return@Canvas

            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                textSize = 16.sp.toPx()
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            val textWidth = paint.measureText(watermarkText)
            val textHeight = paint.fontMetrics.run { descent - ascent }
            val stepX = textWidth + 24.dp.toPx()
            val stepY = textHeight + 28.dp.toPx()

            var colorIndex = 0
            var row = 0
            var y = -paint.fontMetrics.ascent
            while (y < size.height + stepY) {
                val rowOffset = if (row % 2 == 0) 0f else stepX / 2f
                var x = rowOffset
                while (x < size.width + stepX) {
                    paint.color = colors[colorIndex % colors.size]
                        .copy(alpha = 0.5f)
                        .toArgb()
                    colorIndex++
                    drawContext.canvas.nativeCanvas.drawText(watermarkText, x, y, paint)
                    x += stepX
                }
                y += stepY
                row++
            }
        }
    }
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
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val permImages = if (Build.VERSION.SDK_INT >= 33)
        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
    val permVideos = if (Build.VERSION.SDK_INT >= 33)
        Manifest.permission.READ_MEDIA_VIDEO else Manifest.permission.READ_EXTERNAL_STORAGE

    val hasPermission = remember {
        mutableStateOf(
            context.checkSelfPermission(permImages) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(permVideos) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasPermission.value = result.values.all { it }
    }

    data class GalleryItem(
        val uri: Uri,
        val isVideo: Boolean,
        val durationMs: Long,
        val dateAdded: Long,
        val id: Long
    )

    val galleryItems = remember { mutableStateOf<List<GalleryItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasMoreImages by remember { mutableStateOf(true) }
    var hasMoreVideos by remember { mutableStateOf(true) }
    var lastImageDate by remember { mutableLongStateOf(Long.MAX_VALUE) }
    var lastImageId by remember { mutableLongStateOf(Long.MAX_VALUE) }
    var lastVideoDate by remember { mutableLongStateOf(Long.MAX_VALUE) }
    var lastVideoId by remember { mutableLongStateOf(Long.MAX_VALUE) }

    fun loadNextBatch() {
        if (isLoading) return
        if (!hasMoreImages && !hasMoreVideos) return
        isLoading = true

        val newItems = mutableListOf<GalleryItem>()

        if (hasMoreImages) {
            val imageSelection = if (lastImageDate == Long.MAX_VALUE) null
            else "${MediaStore.Images.Media.DATE_ADDED} < ? OR (${MediaStore.Images.Media.DATE_ADDED} = ? AND ${MediaStore.Images.Media._ID} < ?)"
            val imageArgs = if (lastImageDate == Long.MAX_VALUE) null
            else arrayOf(lastImageDate.toString(), lastImageDate.toString(), lastImageId.toString())
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED),
                imageSelection, imageArgs,
                "${MediaStore.Images.Media.DATE_ADDED} DESC, ${MediaStore.Images.Media._ID} DESC"
            )?.use { cursor ->
                var count = 0
                while (cursor.moveToNext() && count < 20) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val dateAdded =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))
                    newItems.add(
                        GalleryItem(
                            uri = Uri.withAppendedPath(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id.toString()
                            ),
                            isVideo = false, durationMs = 0L, dateAdded = dateAdded, id = id,
                        )
                    )
                    lastImageDate = dateAdded
                    lastImageId = id
                    count++
                }
                hasMoreImages = count == 20
            }
        }

        if (hasMoreVideos) {
            val videoSelection = if (lastVideoDate == Long.MAX_VALUE) null
            else "${MediaStore.Video.Media.DATE_ADDED} < ? OR (${MediaStore.Video.Media.DATE_ADDED} = ? AND ${MediaStore.Video.Media._ID} < ?)"
            val videoArgs = if (lastVideoDate == Long.MAX_VALUE) null
            else arrayOf(lastVideoDate.toString(), lastVideoDate.toString(), lastVideoId.toString())
            context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATE_ADDED
                ),
                videoSelection, videoArgs,
                "${MediaStore.Video.Media.DATE_ADDED} DESC, ${MediaStore.Video.Media._ID} DESC"
            )?.use { cursor ->
                var count = 0
                while (cursor.moveToNext() && count < 20) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val duration =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                    val dateAdded =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
                    newItems.add(
                        GalleryItem(
                            uri = Uri.withAppendedPath(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                id.toString()
                            ),
                            isVideo = true, durationMs = duration, dateAdded = dateAdded, id = id,
                        )
                    )
                    lastVideoDate = dateAdded
                    lastVideoId = id
                    count++
                }
                hasMoreVideos = count == 20
            }
        }

        newItems.sortByDescending { it.dateAdded }
        galleryItems.value = galleryItems.value + newItems
        isLoading = false
    }

    LaunchedEffect(hasPermission.value) {
        if (hasPermission.value && galleryItems.value.isEmpty()) {
            loadNextBatch()
        } else if (!hasPermission.value) {
            permissionLauncher.launch(arrayOf(permImages, permVideos))
        }
    }

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@snapshotFlow false
            lastVisible.index >= layoutInfo.totalItemsCount - 4
        }.collect { nearEnd ->
            if (nearEnd && !isLoading && (hasMoreImages || hasMoreVideos)) {
                loadNextBatch()
            }
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
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 16.dp),
        ) {
            if (selectedAttachments.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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

            if (hasPermission.value && galleryItems.value.isNotEmpty()) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(galleryItems.value) { item ->
                        val uriStr = item.uri.toString()
                        val isSelected = selectedAttachments.contains(uriStr)
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = Teal,
                                    shape = RoundedCornerShape(6.dp),
                                )
                                .clickable {
                                    onToggleAttachment(uriStr)
                                },
                        ) {
                            if (item.isVideo) {
                                Box {
                                    VideoThumbnail(
                                        uri = item.uri,
                                        context = context,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(if (isSelected) 5.dp else 6.dp)),
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Black80.copy(alpha = 0.6f)),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = "\u25B6",
                                            color = White,
                                            fontSize = 18.sp,
                                        )
                                    }
                                }
                            } else {
                                AsyncImage(
                                    model = item.uri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(if (isSelected) 5.dp else 6.dp)),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            if (item.isVideo) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(4.dp)
                                        .background(
                                            Black80.copy(alpha = 0.7f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                ) {
                                    Text(
                                        text = formatDuration(item.durationMs),
                                        color = White,
                                        fontSize = 11.sp,
                                        style = regularStyle,
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (!hasPermission.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Разрешите доступ к галерее",
                        style = regularStyle,
                        fontSize = 14.sp,
                        color = Black80,
                    )
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

@Composable
private fun VideoThumbnail(
    uri: Uri,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val bitmap by produceState<Bitmap?>(null, uri) {
        value = withContext(Dispatchers.IO) {
            try {
                val retriever = android.media.MediaMetadataRetriever()
                retriever.setDataSource(context, uri)
                val frame = retriever.frameAtTime
                retriever.release()
                frame
            } catch (_: Exception) {
                null
            }
        }
    }
    bitmap?.let { bmp ->
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}

private fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
private fun Preview_V6() {
    WatermarkOverlay()
}
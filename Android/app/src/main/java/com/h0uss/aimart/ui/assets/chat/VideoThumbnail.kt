package com.h0uss.aimart.ui.assets.chat

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun VideoThumbnail(
    uri: Uri,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(null, uri) {
        value = withContext(Dispatchers.IO) {
            try {
                val retriever = android.media.MediaMetadataRetriever()
                retriever.setDataSource(context, uri)
                val frame = retriever.frameAtTime
                retriever.release()
                frame
            } catch (e: Exception) {
                null
            }
        }
    }
    bitmap?.let { bmp ->
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

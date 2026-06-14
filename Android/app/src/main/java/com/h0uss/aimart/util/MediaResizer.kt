package com.h0uss.aimart.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object MediaResizer {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun resizeAttachment(context: Context, uriString: String): String {
        val uri = uriString.toUri()
        val mimeType = context.contentResolver.getType(uri) ?: return uriString
        return when {
            mimeType.startsWith("image/") -> resizeImage(context, uri)
            mimeType.startsWith("video/") -> resizeVideo(context, uri)
            else -> uriString
        }
    }

    private fun resizeImage(context: Context, uri: Uri): String {
        return try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return uri.toString()
            val fd = pfd.fileDescriptor
            val options = BitmapFactory.Options().apply { inSampleSize = 2 }
            val bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options)
            pfd.close()
            if (bitmap == null) return uri.toString()
            val file = File(context.cacheDir, "protected_img_${System.nanoTime()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
            bitmap.recycle()
            Uri.fromFile(file).toString()
        } catch (_: Exception) {
            uri.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun resizeVideo(context: Context, uri: Uri): String {
        val file = File(context.cacheDir, "protected_vid_${System.nanoTime()}.mp4")
        return try {
            transcodeVideo(context, uri, file)
            if (file.exists() && file.length() > 0) {
                Uri.fromFile(file).toString()
            } else {
                uri.toString()
            }
        } catch (_: Exception) {
            uri.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun transcodeVideo(context: Context, srcUri: Uri, outputFile: File) {
        val extractor = MediaExtractor().also { it.setDataSource(context, srcUri, null) }

        var videoTrack = -1
        var inputFormat: MediaFormat? = null
        for (i in 0 until extractor.trackCount) {
            val fmt = extractor.getTrackFormat(i)
            if (fmt.getString(MediaFormat.KEY_MIME)?.startsWith("video/") == true) {
                videoTrack = i; inputFormat = fmt; break
            }
        }
        if (videoTrack < 0 || inputFormat == null) throw IOException("No video track")
        extractor.selectTrack(videoTrack)

        val mime = inputFormat.getString(MediaFormat.KEY_MIME)!!
        val srcW = inputFormat.getInteger(MediaFormat.KEY_WIDTH)
        val srcH = inputFormat.getInteger(MediaFormat.KEY_HEIGHT)
        if (srcW < 2 || srcH < 2) throw IOException("Video too small")
        val dstW = srcW / 2
        val dstH = srcH / 2
        val frameRate = inputFormat.getInteger(MediaFormat.KEY_FRAME_RATE, 30)
        var bitRate = inputFormat.getInteger(MediaFormat.KEY_BIT_RATE, 2_000_000) / 2
        if (bitRate < 100_000) bitRate = 100_000

        val decoder = MediaCodec.createDecoderByType(mime).apply {
            configure(inputFormat, null, null, 0); start()
        }

        val outputFormat = MediaFormat.createVideoFormat("video/avc", dstW, dstH).apply {
            setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
            setInteger(MediaFormat.KEY_FRAME_RATE, frameRate)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
            setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible
            )
        }
        val encoder = MediaCodec.createEncoderByType("video/avc").apply {
            configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE); start()
        }

        val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        var muxerStarted = false
        var muxerTrack = -1

        val bufferInfo = MediaCodec.BufferInfo()
        var inputDone = false
        var outputDone = false

        while (!outputDone) {
            if (!inputDone) {
                val inIdx = decoder.dequeueInputBuffer(10_000)
                if (inIdx >= 0) {
                    val buf = decoder.getInputBuffer(inIdx)!!
                    val size = extractor.readSampleData(buf, 0)
                    if (size < 0) {
                        decoder.queueInputBuffer(
                            inIdx,
                            0,
                            0,
                            0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                        inputDone = true
                    } else {
                        @Suppress("DEPRECATION")
                        val flags =
                            if ((extractor.sampleFlags and MediaExtractor.SAMPLE_FLAG_SYNC) != 0)
                                MediaCodec.BUFFER_FLAG_SYNC_FRAME else 0
                        decoder.queueInputBuffer(inIdx, 0, size, extractor.sampleTime, flags)
                        extractor.advance()
                    }
                }
            }

            var decoderOutput = false
            while (!decoderOutput) {
                val outIdx = decoder.dequeueOutputBuffer(bufferInfo, 1_000)
                when {
                    outIdx >= 0 -> {
                        val srcImage = decoder.getOutputImage(outIdx)
                        if (srcImage != null && (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) == 0) {
                            val encInIdx = encoder.dequeueInputBuffer(10_000)
                            if (encInIdx >= 0) {
                                val dstImage = encoder.getInputImage(encInIdx)
                                if (dstImage != null) {
                                    scaleYuvImage(srcImage, dstImage)
                                }
                                encoder.queueInputBuffer(
                                    encInIdx,
                                    0,
                                    0,
                                    bufferInfo.presentationTimeUs,
                                    0
                                )
                            }
                        }
                        decoder.releaseOutputBuffer(outIdx, false)
                        decoderOutput = true
                        if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                            val endIdx = encoder.dequeueInputBuffer(10_000)
                            if (endIdx >= 0) {
                                encoder.queueInputBuffer(
                                    endIdx,
                                    0,
                                    0,
                                    bufferInfo.presentationTimeUs,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                )
                            }
                        }
                    }

                    outIdx == MediaCodec.INFO_TRY_AGAIN_LATER -> decoderOutput = true
                    outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {}
                }
            }

            var encoderOutput = false
            while (!encoderOutput) {
                val encOutIdx = encoder.dequeueOutputBuffer(bufferInfo, 1_000)
                when {
                    encOutIdx >= 0 -> {
                        val encBuf = encoder.getOutputBuffer(encOutIdx)!!
                        if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                            // ignore CSD
                        } else if (bufferInfo.size > 0 && muxerStarted) {
                            encBuf.position(bufferInfo.offset)
                            encBuf.limit(bufferInfo.offset + bufferInfo.size)
                            muxer.writeSampleData(muxerTrack, encBuf, bufferInfo)
                        }
                        encoder.releaseOutputBuffer(encOutIdx, false)
                        encoderOutput = true
                        if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            outputDone = true
                        }
                    }

                    encOutIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        if (!muxerStarted) {
                            muxerTrack = muxer.addTrack(encoder.outputFormat)
                            muxer.start()
                            muxerStarted = true
                        }
                    }

                    encOutIdx == MediaCodec.INFO_TRY_AGAIN_LATER -> encoderOutput = true
                }
            }
        }

        runCatching { decoder.stop() }
        decoder.release()
        runCatching { encoder.stop() }
        encoder.release()
        extractor.release()
        if (muxerStarted) runCatching { muxer.stop() }
        muxer.release()
    }

    private fun scaleYuvImage(src: android.media.Image, dst: android.media.Image) {
        val srcPlanes = src.planes
        val dstPlanes = dst.planes
        val dstH = dst.height
        val dstW = dst.width
        val srcH = src.height
        val srcW = src.width

        for (i in 0 until minOf(srcPlanes.size, dstPlanes.size)) {
            val sp = srcPlanes[i]
            val dp = dstPlanes[i]
            val srcBuf = sp.buffer
            val dstBuf = dp.buffer
            dstBuf.clear()

            val planeSrcW: Int
            val planeSrcH: Int
            val planeDstW: Int
            val planeDstH: Int
            if (i == 0) {
                planeSrcW = srcW; planeSrcH = srcH
                planeDstW = dstW; planeDstH = dstH
            } else {
                val assumedChromaW = (srcW + 1) / 2
                val assumedChromaH = (srcH + 1) / 2
                val rowBytes = if (sp.pixelStride == 2) assumedChromaW * 2 else assumedChromaW
                planeSrcW = assumedChromaW
                planeSrcH = if (rowBytes > 0) srcBuf.capacity() / rowBytes else assumedChromaH
                planeDstW = (dstW + 1) / 2; planeDstH = (dstH + 1) / 2
            }

            val scaleX = planeSrcW.toFloat() / planeDstW.toFloat()
            val scaleY = planeSrcH.toFloat() / planeDstH.toFloat()

            for (y in 0 until planeDstH) {
                val srcY = (y * scaleY).toInt().coerceIn(0, planeSrcH - 1)
                val srcRowStart = srcY * sp.rowStride
                val dstRowStart = y * dp.rowStride
                for (x in 0 until planeDstW) {
                    val srcX = (x * scaleX).toInt().coerceIn(0, planeSrcW - 1)
                    for (b in 0 until dp.pixelStride) {
                        val srcPos = srcRowStart + srcX * sp.pixelStride + b
                        val dstPos = dstRowStart + x * dp.pixelStride + b
                        dstBuf.put(dstPos, srcBuf[srcPos])
                    }
                }
            }
        }
    }

    fun saveToDownloads(context: Context, uriString: String): Boolean {
        val uri = uriString.toUri()
        val mimeType = context.contentResolver.getType(uri) ?: return false
        return try {
            when {
                mimeType.startsWith("image/") -> saveMedia(context, uri, mimeType, isVideo = false)
                mimeType.startsWith("video/") -> saveMedia(context, uri, mimeType, isVideo = true)
                else -> false
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun saveMedia(context: Context, uri: Uri, mimeType: String, isVideo: Boolean): Boolean {
        val ext = when {
            mimeType.contains("png") -> ".png"
            mimeType.contains("webp") -> ".webp"
            mimeType.contains("gif") -> ".gif"
            else -> if (isVideo) ".mp4" else ".jpg"
        }
        val fileName = "AIMart_${System.currentTimeMillis()}$ext"

        val resolver = context.contentResolver
        val input = resolver.openInputStream(uri) ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val collection = if (isVideo) MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val dir = if (isVideo) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, "$dir/AIMart")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val outUri = resolver.insert(collection, values) ?: return false
            resolver.openOutputStream(outUri)?.use { output -> input.copyTo(output) }
                ?: return false
            values.clear().also { values.put(MediaStore.Images.Media.IS_PENDING, 0) }
            resolver.update(outUri, values, null, null)
            return true
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(
                if (isVideo) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES
            )
            val dirFile = File(dir, "AIMart").also { it.mkdirs() }
            val file = File(dirFile, fileName)
            FileOutputStream(file).use { output -> input.copyTo(output) }
            val scanIntent = android.content.Intent(
                android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)
            )
            context.sendBroadcast(scanIntent)
            return true
        }
    }
}

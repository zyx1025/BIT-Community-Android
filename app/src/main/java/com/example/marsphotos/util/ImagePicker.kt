package com.example.marsphotos.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever

//上传一张图片
@Composable
fun ImagePickerForSingle(onImagePicked: (Uri) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("选择图片/视频")
    }
}

//上传多张图片
@Composable
fun ImagePickerForList(onImagePicked: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }
    Button(onClick = { launcher.launch("image/*") }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Select Images")
        Text("选择图片")
    }
}


@Composable
fun MediaPicker(onMediaPicked: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onMediaPicked(it) }
    }

    Button(onClick = { launcher.launch("video/*") }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Select Video")
        Text("选择视频")
    }
}


fun getVideoThumbnail(context: Context, videoUri: Uri): Bitmap? {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(context, videoUri)
        // 尝试获取视频的第一帧作为缩略图
        return retriever.getFrameAtTime()
    } catch (e: IllegalArgumentException) {
        // 处理错误情况
        e.printStackTrace()
    } finally {
        retriever.release()
    }
    return null
}
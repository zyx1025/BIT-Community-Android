package com.example.marsphotos.util

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayerComposable(videoUri: Uri) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }

    AndroidView(
        factory = { videoView },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f), // 设置适当的宽高比
    ) { view ->
        view.setVideoURI(videoUri)
        view.start() // 开始播放视频
    }
}

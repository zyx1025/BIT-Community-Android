package com.example.marsphotos.ui.screens.postMenu
/*
    发布topic
 */
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marsphotos.model.request.PostMomentRequest
import com.example.marsphotos.viewmodel.MomentViewModel
import com.example.marsphotos.util.MediaPicker
import com.example.marsphotos.util.VideoPlayerComposable
import com.example.marsphotos.util.getVideoThumbnail
import com.example.marsphotos.util.uploadMediaToOSS

@Composable
fun PostVideo(navController: NavController, viewModel: MomentViewModel) {
    // 状态管理
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var anonymous by rememberSaveable { mutableStateOf(false) }
    var mediaUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf("") }
    val uploadCount = remember { mutableStateOf(0) }
    var showPlayer by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 这里定义发布时刻的UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (!showPlayer) {
            // 显示视频缩略图和播放按钮
            mediaUri?.let { uri ->
                val videoThumbnail = getVideoThumbnail(context, uri)
                videoThumbnail?.let { bitmap ->
                    Box(
                        modifier = Modifier
                            .size(200.dp) // 或其他你希望的尺寸
                            .clip(RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Selected Video",
                            modifier = Modifier.matchParentSize()
                        )
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp) // 或其他你希望的尺寸
                                .clickable { showPlayer = true }
                        )
                    }
                }
            }
        } else {
            // 显示视频播放器组件
            mediaUri?.let { uri ->
                VideoPlayerComposable(videoUri = uri)
            }
        }



        // 图片选择器
        MediaPicker(onMediaPicked = { uri ->
            mediaUri = uri // 添加新选择的图片URI到列表中
        })

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("标题") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("内容") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("匿名")
            Switch(
                checked = anonymous,
                onCheckedChange = { anonymous = it }
            )
        }

        Button(
            onClick = {
                uploadedImageUrl = "" // 清空之前的上传URL
                uploadCount.value = 0 // 重置计数器

                // 仅上传单个视频
                mediaUri?.let { uri ->
                    val objectKey = "user_upload_${System.currentTimeMillis()}.mp4" // 假设视频为mp4格式

                    uploadMediaToOSS(
                        context = context,
                        mediaUri = uri,
                        objectKey = objectKey
                    ) { mediaUrl ->
                        uploadedImageUrl = mediaUrl // 设置上传后的URL
                        uploadCount.value = 1 // 设置计数为1

                        // 构建请求体
                        val requestBody = PostMomentRequest(
                            title = title,
                            content = content,
                            media = listOf(uploadedImageUrl), // 使用单个URL
                            type = 2,
                            anonymous = anonymous,
                            cover_image = uploadedImageUrl + "?x-oss-process=video/snapshot,t_5000,f_png,w_0,h_0,m_fast"
                        )
                        viewModel.postMoment(requestBody) // 发起请求
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("发布")
        }

    }
}
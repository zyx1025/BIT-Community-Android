package com.example.marsphotos.ui.screens.postMenu
/*
    发布时刻
 */
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.marsphotos.model.request.PostMomentRequest
import com.example.marsphotos.viewmodel.MomentViewModel
import com.example.marsphotos.util.ImagePickerForList
import com.example.marsphotos.util.uploadImageToOSS


@Composable
fun PostImage(navController: NavController, viewModel: MomentViewModel) {
    // 状态管理
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var anonymous by rememberSaveable { mutableStateOf(false) }
    var imageUris by rememberSaveable { mutableStateOf(listOf<Uri>()) }
    val uploadedImageUrls = remember { mutableStateListOf<String>() }
    val uploadCount = remember { mutableStateOf(0) }
    val context = LocalContext.current

    // 这里定义发布时刻的UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        // 图片选择器
        ImagePickerForList(onImagePicked = { uri ->
            imageUris = imageUris + uri // 添加新选择的图片URI到列表中
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
                uploadedImageUrls.clear() // 清空之前的上传记录
                uploadCount.value = 0 // 重置计数器

                imageUris.forEachIndexed { index, uri ->
                    val objectKey = "user_upload_${System.currentTimeMillis()}_$index.jpg"
                    uploadImageToOSS(context = context, imageUri = uri, objectKey = objectKey) { imageUrl ->
                        uploadedImageUrls.add(imageUrl) // 添加到上传列表
                        uploadCount.value++ // 增加计数

                        // 当所有图片都上传完成时
                        if (uploadCount.value == imageUris.size) {
                            val requestBody = PostMomentRequest(
                                title = title,
                                content = content,
                                media = uploadedImageUrls,
                                type = 1,
                                anonymous = anonymous,
                                cover_image = uploadedImageUrls[0]
                            )
                            viewModel.postMoment(requestBody) // 发起请求
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "发送")
        }
    }
}




package com.example.marsphotos.ui.screens.moment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.marsphotos.model.moment.Review
import com.example.marsphotos.model.request.PostReviewRequest
import com.example.marsphotos.viewmodel.MomentViewModel

@Composable
fun CommentDialog(review: Review?, momentViewModel: MomentViewModel, momentId:Int,onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }


        Dialog(onDismissRequest = onDismiss) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White) // 这里设置背景颜色为白色
                .padding(16.dp)) {
                Text(text = "评论详情", style = MaterialTheme.typography.h6)

                // 评论输入框
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    },
                    placeholder = { Text("添加评论...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White), // 可以选择是否给TextField也设置背景
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent // 如果TextField有背景色，这里可以设置为Color.White
                    )
                )
                // 发送按钮
                Button(
                    onClick = {
                        // 构造PostReviewRequest对象并调用ViewModel中的方法发送评论
                        val postReviewRequest = PostReviewRequest(content = text, to_momentrid = review?.momentrid)
                        momentViewModel.postReview(postReviewRequest, momentId)
                        onDismiss() // 关闭对话框
                    },
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    // 设置按钮的样式，如颜色、大小等
                ) {
                    Text("发送")
                }
            }
        }

}

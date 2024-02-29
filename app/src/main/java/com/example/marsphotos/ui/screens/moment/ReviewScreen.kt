package com.example.marsphotos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.marsphotos.R
import com.example.marsphotos.model.moment.Review
import com.example.marsphotos.model.moment.SubReview
import com.example.marsphotos.model.moment.toReview
import com.example.marsphotos.viewmodel.MomentViewModel


@Composable
fun ReviewItem(review: Review, momentId: Int, viewModel: MomentViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    viewModel.selectReview(review)
                    viewModel.toggleCommentDialog(true)
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = review.reviewer.avatar),
                contentDescription = "Reviewer Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = review.reviewer.nickname,
                    fontWeight = FontWeight.Bold
                )
                review.content?.let { Text(text = it) }
            }
            LikeButton(is_liked = review.is_liked, likes = review.likes, viewModel = viewModel, momentId = momentId, momentrid = review.momentrid)
        }
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
fun SubReviewItem(subReview: SubReview, momentId: Int, viewModel: MomentViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(start = 24.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .clickable {
                    viewModel.selectReview(subReview.toReview())
                    viewModel.toggleCommentDialog(true)
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = subReview.reviewer.avatar),
                contentDescription = "Reviewer Avatar",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                (if (subReview.target.reviewer.nickname.isNotEmpty()) {
                    "回复 ${subReview.target.reviewer.nickname}: ${subReview.content}"
                } else {
                    subReview.content
                })?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2.copy(fontSize = 12.sp) // 较小的字体大小
                    )
                }
            }
            LikeButton(is_liked = subReview.is_liked, likes = subReview.likes, viewModel = viewModel, momentId = momentId, momentrid = subReview.momentrid)
        }
        Divider(modifier = Modifier.padding(horizontal = 24.dp))
    }
}

@Composable
fun LikeButton(is_liked: Boolean, likes: Int, viewModel: MomentViewModel,momentId: Int,momentrid : Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            // 根据点赞状态调用不同的函数
            if (is_liked) {
                viewModel.delikeReview(momentId,momentrid)
            } else {
                viewModel.likeReview(momentId,momentrid)
            }
        }) {
            // 根据点赞状态显示不同的图标
            val image = if (is_liked) {
                painterResource(id = R.drawable.baseline_favorite_24)
            } else {
                painterResource(id = R.drawable.baseline_favorite_border_24)
            }
            Icon(
                painter = image,
                contentDescription = null,
                tint = Color.Red
            )
        }
        Text(text = "$likes")
    }
}


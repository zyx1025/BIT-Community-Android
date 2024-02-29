package com.example.marsphotos.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.marsphotos.R
import com.example.marsphotos.model.moment.MomentDetails
import com.example.marsphotos.ui.screens.moment.CommentDialog
import com.example.marsphotos.viewmodel.MomentState
import com.example.marsphotos.viewmodel.MomentViewModel

import com.example.marsphotos.viewmodel.ReviewState
import com.example.marsphotos.util.VideoPlayerComposable
import com.example.marsphotos.util.getVideoThumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MomentDetailScreen(
    momentUIState: MomentState,
    reviewState: ReviewState,
    viewModel: MomentViewModel,
    momentId: Int
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        //bottomBar = { BottomBar(momentDetails= momentUIState ,viewModel = viewModel, modifier = Modifier.fillMaxWidth(), scope) }
    ) {
        LazyColumn {
            when (momentUIState) {
                is MomentState.Loading -> item { LoadingScreen(modifier = Modifier.fillParentMaxSize()) }
                is MomentState.Success -> {
                    // 以下是 MomentScreen 中内容的正确嵌入方式
                    item {
                        MomentScreen(momentDetails = momentUIState.detailedMoment,viewModel=viewModel,momentId=momentId)
                    }
                }
                is MomentState.Error -> item { ErrorScreen(modifier = Modifier.fillParentMaxSize(), onReload = { viewModel.getMomentDetails(momentId) }) }
            }


            when (reviewState) {
                is ReviewState.Loading -> item { LoadingScreen(modifier = Modifier.fillParentMaxSize()) }
                is ReviewState.Success -> {
                    // AllReviewScreen的内容也应该是一个个item，而不是嵌套一个LazyColumn
                    item {
                        Text(text = "共 ${reviewState.allReview.num} 条评论")
                    }
                    items(reviewState.allReview.reviews) { review ->
                        ReviewItem(review = review,momentId, viewModel)
                        review.sub_reviews?.forEach { subReview ->
                            SubReviewItem(subReview = subReview, momentId, viewModel)
                        }
                    }
                }
                is ReviewState.Error -> item { ErrorScreen(modifier = Modifier.fillParentMaxSize(), onReload = { viewModel.getAllReview(momentId) }) }
            }
        }
    }


    if (viewModel.showCommentDialog.value) {
        CommentDialog(
            review = viewModel.selectedReview.value,
            onDismiss = { viewModel.toggleCommentDialog(false) },
            momentId = momentId,
            momentViewModel = viewModel
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MomentScreen(momentDetails: MomentDetails, modifier: Modifier = Modifier, viewModel: MomentViewModel, momentId: Int) {

    var showPlayer by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mediaUri by rememberSaveable { mutableStateOf<Uri?>(Uri.parse(momentDetails.media[0])) }
    var videoThumbnail by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(momentDetails.media[0]) {
        videoThumbnail = withContext(Dispatchers.IO) {
            getVideoThumbnail(context, Uri.parse(momentDetails.media[0]))
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // 头像和昵称
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = momentDetails.user.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = momentDetails.user.nickname,
                fontWeight = FontWeight.Bold
            )
        }
        // 显示第一张图片，可拖动查看其他图片

        if(momentDetails.type!=2){
            val pagerState = rememberPagerState(pageCount = { momentDetails.media.size })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) { page ->
                Image(
                    painter = rememberAsyncImagePainter(model = momentDetails.media[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }else{
                // 显示视频播放器组件
                mediaUri?.let { uri ->
                    VideoPlayerComposable(videoUri = uri)
                }

        }


        Spacer(modifier = Modifier.width(8.dp))

        // 内容标题和文本
        Text(
            text = momentDetails.title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = momentDetails.content,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            fontSize = 12.sp
        )
        // 喜欢按钮和数量
        Row(
            modifier = Modifier.fillMaxWidth(), // 确保Row填满水平空间
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // 使用SpaceBetween来分隔子项
        ) {
            // 左侧IconButton
            IconButton(onClick = {
                if (momentDetails.is_liked) {
                    viewModel.delikeMoment(momentId)
                } else {
                    viewModel.likeMoment(momentId)
                }
            }) {
                val image = if (momentDetails.is_liked) {
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
            Text(text = "${momentDetails.likes}")

            Spacer(modifier = Modifier.weight(1f)) // 这个Spacer会推动右侧的IconButton到最右边

            // 右侧IconButton
            IconButton(onClick = {
                viewModel.selectReview(null)
                viewModel.toggleCommentDialog(true)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_comment_24),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }



        Text(
            text = "最近更新于: ${momentDetails.update_time}",
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}


/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.marsphotos.R
import com.example.marsphotos.model.moment.Moment
import com.example.marsphotos.model.moment.Moments
import com.example.marsphotos.viewmodel.AllMomentState
import com.example.marsphotos.viewmodel.MomentViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
@Composable
fun HomeScreen(
    allMomentState: AllMomentState,
    viewModel: MomentViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (allMomentState) {
        is AllMomentState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is AllMomentState.Success -> AllMomentScreen(
            moments = allMomentState.moments,
            viewModel = viewModel,
            navController = navController,
            modifier = modifier.fillMaxWidth()
        )
        is AllMomentState.Error -> ErrorScreen(
            modifier = modifier.fillMaxSize(),
            onReload = { viewModel.getMoment(offset = 0) } // 传入ViewModel的getMoment方法
        )
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun AllMomentScreen(
    moments: Moments,
    viewModel: MomentViewModel,
    navController: NavController,
    modifier: Modifier = Modifier // 这里的modifier参数是可选的，默认值是Modifier
) {
    val navigateToDetails by viewModel.navigateToDetails.observeAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    navigateToDetails?.let { momentId ->
        LaunchedEffect(momentId) {
            navController.navigate("momentDetails/$momentId")
            viewModel.resetNavigation()
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getMoment(offset = 0)
                isRefreshing = false
            }
        }
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier // 直接使用传入的modifier
        ) {
            items(moments.moments) { moment ->
                MomentCard(moment, viewModel)
            }
        }
    }
}


/*
单独渲染每一条记录
 */
@Composable
fun MomentCard(moment: Moment, viewModel: MomentViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.onMomentSelected(moment.momentid)
            }
    ){
        Column {
            Image(
                painter = if (moment.cover_image.isNullOrEmpty()) {
                    painterResource(id = R.drawable.no_image_available) // Replace with the resource ID of the downloaded image
                } else {
                    rememberAsyncImagePainter(model = moment.cover_image)
                },
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(model = moment.user.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = moment.user.nickname,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = moment.title ?: "无题",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        // 根据点赞状态调用不同的函数
                        if (moment.is_liked) {
                            viewModel.delikeMoment(moment.momentid)
                        } else {
                            viewModel.likeMoment(moment.momentid)
                        }
                    }) {
                        // 根据点赞状态显示不同的图标
                        val image = if (moment.is_liked) {
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
                    Text(text = "${moment.likes}")
                }
            }
        }
    }
}
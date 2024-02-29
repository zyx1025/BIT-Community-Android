package com.example.marsphotos.ui.screens.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.marsphotos.model.topic.Topics
import com.example.marsphotos.ui.screens.ErrorScreen
import com.example.marsphotos.ui.screens.LoadingScreen
import com.example.marsphotos.viewmodel.TopicState
import com.example.marsphotos.viewmodel.TopicViewModel

@Composable
fun TopicScreen(
    topicState: TopicState,
    viewModel: TopicViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (topicState) {
        is TopicState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is TopicState.Success -> TopicMenu(
            topics = topicState.topics,  // 这里传入成功状态携带的moments数据
            viewModel = viewModel,          // 传入ViewModel
            navController = navController,  // 传入NavController
            modifier = modifier.fillMaxWidth() // 这里的modifier可以直接传递，不需要再次调用fillMaxWidth
        )
        is TopicState.Error -> ErrorScreen(modifier = modifier.fillMaxSize(), onReload = { viewModel.getTopic() })
    }
}


@Composable
fun TopicMenu(
    topics: Topics,
    viewModel: TopicViewModel,
    navController: NavController,
    modifier: Modifier = Modifier // 这里的modifier参数是可选的，默认值是Modifier
) {
    val navigateToDetails by viewModel.navigateToDetails.observeAsState()

    //定义点击子项的跳转函数
    navigateToDetails?.let { topicId ->
        LaunchedEffect(topicId) {
            navController.navigate("topics/$topicId")
            viewModel.resetNavigation()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier // 直接使用传入的modifier
    ) {
        items(topics.topics) { topic ->
            //MomentCard(moment, viewModel)
        }
    }
}

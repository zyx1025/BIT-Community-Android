package com.example.marsphotos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marsphotos.model.Screen


@Composable
fun MyTopAppBar(navController: NavController,onNavigationIconClick: () -> Unit = { navController.popBackStack() }) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val isPostMenuScreen = navController.currentBackStackEntryAsState().value?.destination?.route == Screen.PostMenu.route || navController.currentBackStackEntryAsState().value?.destination?.route == "postRating" || navController.currentBackStackEntryAsState().value?.destination?.route == "postMoment"

    TopAppBar(
        title = {
            if (isPostMenuScreen) {
                // 使"发布时刻"和"发布评分"居中并保持一定距离
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("图片", Modifier.clickable {
                        // 显示发布时刻的UI
                        navController.navigate("postMoment")
                    })
                    Spacer(Modifier.width(40.dp))
                    Text("视频", Modifier.clickable {
                        // 显示发布评分的UI
                        navController.navigate("postRating")
                    })
                }
            }  else {
                // 其他页面显示应用的标题

            }
        },
        modifier = Modifier
            .statusBarsPadding(), // 适应状态栏的padding

        navigationIcon = {
            IconButton(onClick = {
                when (currentDestination) {
                    "login" -> {
                        // 在登录界面，点击返回应该导航到UserMenu界面
                        navController.navigate(Screen.UserMenu.route) {
                            popUpTo(navController.graph.startDestinationRoute!!) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                    else -> {
                        // 对于其他界面，使用默认的回退操作
                        navController.popBackStack()
                    }
                }
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            } }
        ,
        backgroundColor = Color.White
    )
}




//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopAppBar(navController: NavHostController) {
//    // 使用 mutableStateOf 创建一个可观察的状态
//    val isStartDestination = remember { mutableStateOf(true) }
//
//    // 使用 LaunchedEffect 来监听 navController 的变化
//    navController.addOnDestinationChangedListener { _, destination, _ ->
//        isStartDestination.value = destination.route == navController.graph.findStartDestination().route
//    }
//
//
//    TopAppBar(
//        title = { /* 标题, 如果需要的话 */ },
//        navigationIcon = {
//            // 当不是起始目的地时显示回退按钮
//            if (!isStartDestination.value) {
//                IconButton(onClick = {
//                    navController.popBackStack()
//                }) {
//                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                }
//            }
//        },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = Color(0xFF8BC34A)
//        )
//    )
//}
//
//

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

package com.example.marsphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marsphotos.model.Screen

import com.example.marsphotos.ui.screens.BottomNavigationBar
import com.example.marsphotos.ui.screens.HomeScreen
import com.example.marsphotos.ui.screens.MomentDetailScreen
import com.example.marsphotos.ui.screens.MyTopAppBar

import com.example.marsphotos.ui.screens.UserLoginView
import com.example.marsphotos.ui.screens.UserRegisterView
import com.example.marsphotos.ui.screens.postMenu.PostImage

import com.example.marsphotos.ui.screens.postMenu.PostVideo
import com.example.marsphotos.ui.screens.topic.TopicScreen
import com.example.marsphotos.ui.screens.user.UserMenu
import com.example.marsphotos.ui.theme.MarsPhotosTheme

import com.example.marsphotos.viewmodel.MomentViewModel
import com.example.marsphotos.viewmodel.TopicViewModel
import com.example.marsphotos.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MarsPhotosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { MyTopAppBar(navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(navController, startDestination = "homeGraph") {
                navigation(startDestination = Screen.Home.route, route = "homeGraph") {
                    composable(Screen.Home.route) {
                        val momentViewModel: MomentViewModel = viewModel()
                        LaunchedEffect(Unit) {
                            momentViewModel.getMoment(offset = momentViewModel.offset) // 每次进入时都重新加载
                        }
                        HomeScreen(
                            allMomentState = momentViewModel.allMomentState,
                            viewModel = momentViewModel,
                            navController = navController
                        )
                    }

                    composable(
                        route = "momentDetails/{momentId}",
                        arguments = listOf(navArgument("momentId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val momentId = backStackEntry.arguments?.getInt("momentId")
                        if (momentId != null) {
                            val momentViewModel: MomentViewModel = viewModel()
                            LaunchedEffect(momentId) {
                                momentViewModel.getMomentDetails(momentId)
                                momentViewModel.getAllReview(momentId)
                            }

                            MomentDetailScreen(
                                momentId = momentId,
                                momentUIState = momentViewModel.momentState,
                                reviewState = momentViewModel.reviewState,
                                viewModel = momentViewModel
                            )
                        }
                    }
                }

                navigation(startDestination = Screen.PostMenu.route, route = "postMenuGraph") {
                    composable(Screen.PostMenu.route) {
                        val momentViewModel: MomentViewModel = viewModel()
                        PostImage(navController, momentViewModel)
                    }
                    composable("postMoment") { PostImage(navController, viewModel = viewModel()) }
                    composable("postRating") { PostVideo(navController, viewModel = viewModel()) }
                }

                navigation(startDestination = Screen.UserMenu.route, route = "userMenuGraph") {
                    composable(Screen.UserMenu.route) {
                        val userViewModel: UserViewModel = viewModel()
                        UserMenu(navController, userViewModel)
                    }
                    composable("login") {
                        val userViewModel: UserViewModel = viewModel()
                        UserLoginView(navController, userViewModel)
                    }
                    composable("register") {
                        val userViewModel: UserViewModel = viewModel()
                        UserRegisterView(navController, userViewModel)
                    }
                    // 定义 Screen.UserMenu 的其它子页面
                }

                navigation(startDestination = Screen.Topic.route, route = "topicMenuGraph") {
                    composable(Screen.Topic.route) {
                        val topicViewModel: TopicViewModel = viewModel()
                        LaunchedEffect(Unit) {
                            topicViewModel.getTopic()
                        }
                        TopicScreen(
                            topicState = topicViewModel.topicState,
                            viewModel = topicViewModel,
                            navController = navController
                        )

                        //其它子页面
                    }


                }
            }
        }
    }
}



package com.example.marsphotos.ui.screens
/*
    底部导航栏
 */
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marsphotos.model.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color.White // 设置背景色为白色
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        //首页
        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.Home.route) Color.Red else Color.Black
                Icon(Icons.Default.Home, contentDescription = "首页", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.Home.route) Color.Red else Color.Black
                Text("首页", color = textColor)
            },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        //评分
//        BottomNavigationItem(
//            icon = {
//                val tint = if (currentRoute == Screen.Topic.route) Color.Red else Color.Black
//                Icon(Icons.Default.Star, contentDescription = "评分", tint = tint)
//            },
//            label = {
//                val textColor = if (currentRoute == Screen.Topic.route) Color.Red else Color.Black
//                Text("评分", color = textColor)
//            },
//            selected = currentRoute == Screen.Topic.route,
//            onClick = {
//                navController.navigate(Screen.Topic.route) {
//                    popUpTo(navController.graph.findStartDestination().id) {
//                        saveState = true
//                    }
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }
//        )

        //发布界面
        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.PostMenu.route) Color.Red else Color.Black
                Icon(Icons.Default.Add, contentDescription = "发布", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.PostMenu.route) Color.Red else Color.Black
                Text("发布", color = textColor)
            },
            selected = currentRoute == Screen.PostMenu.route,
            onClick = {
                navController.navigate(Screen.PostMenu.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )


        //用户界面
        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.UserMenu.route) Color.Red else Color.Black
                Icon(Icons.Default.Person, contentDescription = "UserMenu", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.UserMenu.route) Color.Red else Color.Black
                Text("我的", color = textColor)
            },
            selected = currentRoute == Screen.UserMenu.route,
            onClick = {
                navController.navigate(Screen.UserMenu.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )


    }
}
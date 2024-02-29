package com.example.marsphotos.model

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object UserMenu : Screen("user")
    object PostMenu : Screen("post")
    object Topic : Screen("topic")
}

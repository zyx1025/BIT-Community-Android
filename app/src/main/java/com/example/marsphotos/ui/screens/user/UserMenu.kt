package com.example.marsphotos.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.marsphotos.model.User
import com.example.marsphotos.viewmodel.UserViewModel


@Composable
fun UserMenu(navController: NavController, userViewModel: UserViewModel) {
    val userSession = userViewModel.userSession.collectAsState().value

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userSession != null) {
                UserLoggedInView(userSession, onLogout = {
                    userViewModel.logoutUser()
                })
            } else {
                UserLoggedOutView {
                    navController.navigate("login")
                }
            }
        }
    }
}

@Composable
fun UserLoggedInView(userSession: User?, onLogout: () -> Unit) {
    if (userSession == null){
        return
    }
    Image(
        painter = rememberAsyncImagePainter(userSession.avatar),
        contentDescription = "Avatar",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = userSession.nickname, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    // Other user details can be added here
    Button(onClick = onLogout) {
        Text(text = "退出")
    }
}

@Composable
fun UserLoggedOutView(onLoginNavigate: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Placeholder for the user avatar, since we do not have the exact drawable
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            // Icon or image placeholder
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Login Placeholder",
                modifier = Modifier.size(60.dp),
                tint = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "点击登录",
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.clickable(onClick = onLoginNavigate)
        )
    }
}


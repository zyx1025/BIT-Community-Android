package com.example.marsphotos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marsphotos.viewmodel.UserViewModel
import java.security.MessageDigest

@Composable
fun UserLoginView(navController: NavController, onLoginClicked: UserViewModel) {
    // state for managing user input
    var studentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = studentId,
            onValueChange = { studentId = it },
            label = { Text("学号") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val encryptedPassword = md5(password) // 对密码进行MD5加密
                onLoginClicked.loginUser(studentId, encryptedPassword, navController) // 使用加密后的密码进行登录
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }
        Text(
            text = "注册",
            color = Color.Gray,
            modifier = Modifier.clickable {
                // 实现显示注册界面的逻辑
                navController.navigate("register")
            }
        )
    }
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())
    return digest.joinToString("") {
        String.format("%02x", it)
    }
}
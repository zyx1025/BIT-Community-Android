package com.example.marsphotos.model.request
/*
处理管理功能：用户的全部请求类型
 */
import com.example.marsphotos.model.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val bitid: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val bitid: String,
    val password: String
)



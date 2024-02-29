package com.example.marsphotos.model.response

import com.example.marsphotos.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseForLogin(
    val code: Int,
    val msg: String,
    val data: User?
)

@Serializable
data class ApiResponseForRegister(
    val code: Int,
    val msg: String,
    val data: User?
)
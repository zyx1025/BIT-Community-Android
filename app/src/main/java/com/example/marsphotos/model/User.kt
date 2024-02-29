package com.example.marsphotos.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val nickname: String,
    val avatar: String,
    val uid: Int,
    val token: String?
)

@Serializable
data class UserShow(
    val uid: Int,
    val avatar: String,
    val nickname: String
)


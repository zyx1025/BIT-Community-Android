package com.example.marsphotos.model.request
/*
处理功能1：moment的全部请求类型
 */
import kotlinx.serialization.Serializable

@Serializable
data class PostMomentRequest(
    val title: String,
    val content: String,
    val media : List<String>,
    val anonymous: Boolean,
    val type: Int,
    val cover_image: String
)

@Serializable
data class PostReviewRequest(
    val content: String,
    val to_momentrid: Int?
)
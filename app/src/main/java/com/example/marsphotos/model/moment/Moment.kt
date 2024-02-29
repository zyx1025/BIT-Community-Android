package com.example.marsphotos.model.moment

import com.example.marsphotos.model.UserShow
import kotlinx.serialization.Serializable

@Serializable
data class Moment(
    val cover_image: String?,
    val type: Int,
    val momentid: Int,
    val title: String?,
    val likes: Int,
    val user: UserShow,
    val is_liked: Boolean
)

/*
某个moment的详细信息
 */
@Serializable
data class MomentDetails(
    val media: List<String>,
    val type: Int,
    val title: String,
    val content: String,
    val create_time: String,
    val update_time: String,
    val likes: Int,
    val user: UserShow,
    val is_liked:Boolean
)

@Serializable
data class Moments(
    val moments: List<Moment>,
    val num: Int
)
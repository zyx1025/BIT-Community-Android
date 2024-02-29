package com.example.marsphotos.model.moment

import kotlinx.serialization.Serializable

@Serializable
data class ReviewSum(
    val reviews: List<Review>,
    val num: Int
)

@Serializable
data class Review(
    val content: String?,
    val create_time: String?,
    val likes: Int,
    val is_liked: Boolean,
    val momentrid: Int,

    val reviewer: Reviewer,
    val sub_reviews: List<SubReview>?,
    val total_sub_reviews: Int
)

fun SubReview.toReview(): Review {
    return Review(
        content = this.content,
        create_time = this.create_time,
        likes = this.likes,
        is_liked = this.is_liked,
        momentrid = this.momentrid,
        reviewer = this.reviewer,
        // 对于Review特有的字段，赋值为null或默认值
        sub_reviews = null,
        total_sub_reviews = 0,
    )
}

@Serializable
data class Reviewer(
    val uid: Int,
    val nickname: String,
    val avatar: String
)


@Serializable
data class SubReview(
    val content: String?,
    val create_time: String?,
    val is_liked: Boolean,
    val likes: Int,
    val momentrid: Int,
    val reviewer: Reviewer,
    val target: SubReviewTarget,
)

@Serializable
data class SubReviewTarget(
    val momentrid: Int?,
    val reviewer: Reviewer
)


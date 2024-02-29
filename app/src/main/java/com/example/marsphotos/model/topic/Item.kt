package com.example.marsphotos.model.topic

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val topics: List<Topic>,
    val num: Int
)

@Serializable
data class MaxItem (
    val rated_item_id: Int,
    val name: String,
    val img: String,
    val description: String,
    val uid: Int,
    val maxLikesReview: String,
    val score: Double,
    val peopleScored: Int
)
package com.example.marsphotos.model.topic

import com.example.marsphotos.model.UserShow
import com.example.marsphotos.model.moment.Moment
import kotlinx.serialization.Serializable
import java.lang.invoke.TypeDescriptor

@Serializable
data class Topics(
    val topics: List<Topic>,
    val num: Int
)

@Serializable
data class Topic(
    val id: Int,
    val name: String,
    val img: String,
    val description: String,
    val uid: String,
    val people_scored: Int,
    val max_item: List<MaxItem>
)
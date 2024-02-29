package com.example.marsphotos.model.response
/*
处理功能2：topic的全部响应类型
 */
import com.example.marsphotos.model.moment.Moments
import com.example.marsphotos.model.topic.Topics
import kotlinx.serialization.Serializable

/*
  GET /moments:查看所有moment
 */
@Serializable
data class ApiResponseForTopic(
    val code: Int,
    val msg: String,
    val data: Topics
)

///*
//  POST /moments:发表一个帖子
// */
//@Serializable
//data class ApiResponseForPostMoments(
//    val code: Int,
//    val msg: String,
//)
//
///*
//    GET /moments/{momentid}:查看单个moment
// */
//@Serializable
//data class ApiResponseForOneMoments(
//    val code: Int,
//    val msg: String,
//    val data: MomentDetails
//)
//
///*
//    DELETE /moments/{moments}:删除某个moment
// */
//@Serializable
//data class ApiResponseForDeleteMoments(
//    val code: Int,
//    val message: String,
//)
//
///*
//    GET /moments/{momentid}/reviews:查看评论
// */
//@Serializable
//data class ApiResponseForGetReview(
//    val code: Int,
//    val msg: String,
//    val data: ReviewSum
//)
//
///*
//    POST /moments/{momentid}/reviews:添加评论
// */
//@Serializable
//data class ApiResponseForPostReview(
//    val code: Int,
//    val message: String,
//)
//
///*
//    DELETE /moments/{momentid}/reviews/{momentrid}:删除某个评论
// */
//@Serializable
//data class ApiResponseForDeleteReview(
//    val code: Int,
//    val message: String,
//)
//
///*
//    POST /moments/{momentid}/likes:给moment点赞
// */
//@Serializable
//data class ApiResponseForLikeMoment(
//    val code: Int,
//    val msg: String,
//)
//
///*
//    DELETE /moments/{momentid}/likes:删除之前的moment点赞
// */
//@Serializable
//data class ApiResponseForDeLikeMoment(
//    val code: Int,
//    val msg: String,
//)
//
//
///*
//    POST /moments/{momentid}/reviews/{momentrid}/likes:给评论点赞
// */
//@Serializable
//data class ApiResponseForLikeReview(
//    val code: Int,
//    val msg: String,
//)
//
//
///*
//    DELETE /moments/{momentid}/reviews/{momentrid}/likes:删除之前的评论点赞
// */
//@Serializable
//data class ApiResponseForDeLikeReview(
//    val code: Int,
//    val msg: String,
//)
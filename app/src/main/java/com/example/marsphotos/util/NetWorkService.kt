/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.marsphotos.util

import com.example.marsphotos.model.request.LoginRequest
import com.example.marsphotos.model.request.PostMomentRequest
import com.example.marsphotos.model.request.PostReviewRequest
import com.example.marsphotos.model.request.RegisterRequest
import com.example.marsphotos.model.response.ApiResponseForDeLikeMoment
import com.example.marsphotos.model.response.ApiResponseForDeLikeReview
import com.example.marsphotos.model.response.ApiResponseForDeleteMoments
import com.example.marsphotos.model.response.ApiResponseForDeleteReview
import com.example.marsphotos.model.response.ApiResponseForGetReview
import com.example.marsphotos.model.response.ApiResponseForLikeMoment
import com.example.marsphotos.model.response.ApiResponseForLikeReview
import com.example.marsphotos.model.response.ApiResponseForLogin
import com.example.marsphotos.model.response.ApiResponseForMoments
import com.example.marsphotos.model.response.ApiResponseForOneMoments
import com.example.marsphotos.model.response.ApiResponseForPostMoments
import com.example.marsphotos.model.response.ApiResponseForPostReview
import com.example.marsphotos.model.response.ApiResponseForRegister
import com.example.marsphotos.model.response.ApiResponseForTopic
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://182.92.160.121:8080/"


interface UserApiService {
    @POST("/users/login")
    suspend fun loginUser(@Body requestBody: LoginRequest): ApiResponseForLogin

    @POST("/users/register")
    suspend fun registerUser(@Body requestBody: RegisterRequest): ApiResponseForRegister
}

interface MomentApiService {
    @GET("moments")
    suspend fun getAllMoments(@Header("Authorization") token: String,@Query("limit") limit: Int, @Query("offset") offset: Int): ApiResponseForMoments


    @GET("moments/{momentid}")
    suspend fun getMoment(@Header("Authorization") token: String,@Path("momentid") momentid: Int): ApiResponseForOneMoments

    @POST("moments")
    suspend fun postMoment(@Header("Authorization") token: String,@Body requestBody: PostMomentRequest): ApiResponseForPostMoments
    //参数为ReviewDetails类型

    @DELETE("moments/{momentid}")
    suspend fun deleteMoment(@Path("momentid") momentid: Int): ApiResponseForDeleteMoments

    @POST("moments/{momentid}/likes")
    suspend fun postLikeToMoment(@Header("Authorization") token: String, @Path("momentid") momentid: Int): ApiResponseForLikeMoment

    @DELETE("moments/{momentid}/likes")
    //String类型存疑
    suspend fun deleteLikeToMoment(@Header("Authorization") token: String,@Path("momentid") momentid: Int): ApiResponseForDeLikeMoment

    @GET("moments/{momentid}/reviews")
    suspend fun getReviews(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Query("limit") limit: Int, @Query("offset") offset: Int): ApiResponseForGetReview

    @POST("moments/{momentid}/reviews")
    suspend fun postReviews(@Header("Authorization") token: String, @Body requestBody: PostReviewRequest, @Path("momentid") momentid: Int): ApiResponseForPostReview

    @DELETE("moments/{momentid}/reviews/{momentrid}")
    suspend fun deleteReview(@Path("momentid") momentid: String,@Path("momentrid") momentrid: String): ApiResponseForDeleteReview

    @POST("/moments/{momentid}/reviews/{momentrid}/likes")
    suspend fun postLikeToReview(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Path("momentrid") momentrid: Int): ApiResponseForLikeReview

    @DELETE("/moments/{momentid}/reviews/{momentrid}/likes")
    suspend fun deleteLikeToReview(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Path("momentrid") momentrid: Int): ApiResponseForDeLikeReview
}


/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object InterfaceApi {
    val retrofitServiceForUser: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val retrofitServiceForMoments: MomentApiService by lazy {
        retrofit.create(MomentApiService::class.java)
    }

    val retrofitServiceForTopic: TopicApiService by lazy {
        retrofit.create(TopicApiService::class.java)
    }
}

fun getMomentApiService(): MomentApiService {
    return retrofit.create(MomentApiService::class.java)
}



interface TopicApiService {
    @GET("topics")
    suspend fun getTopics(@Query("limit") limit: Int, @Query("offset") offset: Int): ApiResponseForTopic

    @GET("moments/{momentid}")
    suspend fun getMoment(@Header("Authorization") token: String,@Path("momentid") momentid: Int): ApiResponseForOneMoments

    @POST("moments")
    suspend fun postMoment(@Header("Authorization") token: String,@Body requestBody:PostMomentRequest): ApiResponseForPostMoments
    //参数为ReviewDetails类型

    @DELETE("moments/{momentid}")
    suspend fun deleteMoment(@Path("momentid") momentid: Int): ApiResponseForDeleteMoments

    @POST("moments/{momentid}/likes")
    suspend fun postLikeToMoment(@Header("Authorization") token: String, @Path("momentid") momentid: Int): ApiResponseForLikeMoment

    @DELETE("moments/{momentid}/likes")
    //String类型存疑
    suspend fun deleteLikeToMoment(@Header("Authorization") token: String,@Path("momentid") momentid: Int): ApiResponseForDeLikeMoment

    @GET("moments/{momentid}/reviews")
    suspend fun getReviews(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Query("limit") limit: Int, @Query("offset") offset: Int): ApiResponseForGetReview

    @POST("moments/{momentid}/reviews")
    suspend fun postReviews(@Path("momentid") momentid: String): ApiResponseForPostReview

    @DELETE("moments/{momentid}/reviews/{momentrid}")
    suspend fun deleteReview(@Path("momentid") momentid: String,@Path("momentrid") momentrid: String): ApiResponseForDeleteReview

    @POST("/moments/{momentid}/reviews/{momentrid}/likes")
    //String类型存疑
    suspend fun postLikeToReview(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Path("momentrid") momentrid: Int): ApiResponseForLikeReview

    @DELETE("/moments/{momentid}/reviews/{momentrid}/likes")
    //String类型存疑
    suspend fun deleteLikeToReview(@Header("Authorization") token: String,@Path("momentid") momentid: Int,@Path("momentrid") momentrid: Int): ApiResponseForDeLikeReview

}
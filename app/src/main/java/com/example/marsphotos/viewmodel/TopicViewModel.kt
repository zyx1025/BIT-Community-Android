package com.example.marsphotos.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.moment.MomentDetails
import com.example.marsphotos.model.topic.Topics
import com.example.marsphotos.util.InterfaceApi
import com.example.marsphotos.util.UserSessionManager
import kotlinx.coroutines.launch

/*
本类封装所有与功能2：topic有关的方法
 */

sealed interface TopicState {
    data class Success(var topics: Topics) : TopicState
    object Error : TopicState
    object Loading : TopicState
}
sealed interface ItemState {
    data class Success(val detailedMoment: MomentDetails) : ItemState
    object Error : ItemState
    object Loading : ItemState
}
//sealed interface ReviewState {
//    data class Success(val allReview: ReviewSum) : ReviewState
//    object Error : ReviewState
//    object Loading : ReviewState
//}



class TopicViewModel(application: Application) : AndroidViewModel(application) {

    var topicState: TopicState by mutableStateOf(TopicState.Loading)
    var momentState: ItemState by mutableStateOf(ItemState.Loading)
    //var reviewState: ReviewState by mutableStateOf(ReviewState.Loading)

        private set

    private val userSessionManager = UserSessionManager.getInstance(application)

    //加载全部时刻
    fun getTopic() {
        viewModelScope.launch {
            try {
                val response = InterfaceApi.retrofitServiceForTopic.getTopics( limit = 10, offset = 0)
                topicState = TopicState.Success(response.data)
            } catch (e: Exception) {
                Log.e("Cart", "Failed to fetch cart", e)
                topicState = TopicState.Error
            }
        }
    }



//    fun postMoment(requestBody: PostMomentRequest) {
//        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
//        if (userToken != null) {
//            viewModelScope.launch {
//                try {
//                    val response = InterfaceApi.retrofitServiceForMoments.postMoment("Bearer $userToken",requestBody)
//                    if(response.code!=0){
//                        Log.e("发布失败", response.msg)
//                        Toast.makeText(getApplication(),"发布失败: ${response.msg}", Toast.LENGTH_SHORT).show()
//                    }else{
//                        Toast.makeText(getApplication(),"发布成功: ${response.msg}", Toast.LENGTH_SHORT).show()
//                        Log.e("发布成功", response.msg)
//                        getMoment()
//                    }
//                } catch (e: Exception) {
//                    Log.e("LikeMoment", "Failed to like moment", e)
//                }
//            }
//        }else{
//            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
//        }
//    }

    private val _navigateToDetails = MutableLiveData<Int?>()
    val navigateToDetails: LiveData<Int?> = _navigateToDetails

    fun resetNavigation() {
        _navigateToDetails.value = null
    }

    fun onMomentSelected(momentId: Int) {
        _navigateToDetails.value = momentId
    }


}
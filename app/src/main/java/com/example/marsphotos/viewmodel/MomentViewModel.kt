package com.example.marsphotos.viewmodel
/*
本类封装所有与功能1：moment有关的方法
 */
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.moment.MomentDetails
import com.example.marsphotos.model.moment.Moments
import com.example.marsphotos.model.moment.Review
import com.example.marsphotos.model.moment.ReviewSum
import com.example.marsphotos.model.request.PostMomentRequest
import com.example.marsphotos.model.request.PostReviewRequest
import com.example.marsphotos.util.InterfaceApi.retrofitServiceForMoments
import com.example.marsphotos.util.UserSessionManager
import kotlinx.coroutines.launch

sealed interface AllMomentState {
    data class Success(var moments: Moments) : AllMomentState
    object Error : AllMomentState
    object Loading : AllMomentState
}
sealed interface MomentState {
    data class Success(val detailedMoment: MomentDetails) : MomentState
    object Error : MomentState
    object Loading : MomentState
}
sealed interface ReviewState {
    data class Success(val allReview: ReviewSum) : ReviewState
    object Error : ReviewState
    object Loading : ReviewState
}

class MomentViewModel(application: Application) : AndroidViewModel(application) {

    var allMomentState: AllMomentState by mutableStateOf(AllMomentState.Loading)
    var momentState: MomentState by mutableStateOf(MomentState.Loading)
    var reviewState: ReviewState by mutableStateOf(ReviewState.Loading)
    var offset by mutableStateOf(0)
        private set

    private val _showCommentDialog = mutableStateOf(false)
    val showCommentDialog: State<Boolean> = _showCommentDialog

    fun toggleCommentDialog(show: Boolean) {
        Log.d("是否展示",show.toString())
        _showCommentDialog.value = show
        Log.d("是否展示1",showCommentDialog.value.toString())
    }

    // 您可能还需要存储被选中评论的信息以供Dialog使用
    private val _selectedReview = mutableStateOf<Review?>(null)
    val selectedReview: State<Review?> = _selectedReview

    fun selectReview(review: Review?) {
        _selectedReview.value = review
    }



    private val userSessionManager = UserSessionManager.getInstance(application)

    // 加载全部时刻
    fun getMoment(limit: Int = 100, offset: Int) {
        viewModelScope.launch {
            try {
                val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
                val response = retrofitServiceForMoments.getAllMoments("Bearer $userToken", limit, offset)
                allMomentState = AllMomentState.Success(response.data)
                Log.d("getAllPosts", response.data.toString())
            } catch (e: Exception) {
                Log.e("Error", "Failed to fetch moments", e)
                allMomentState = AllMomentState.Error
            }
        }
    }

    fun getMomentDetails(momentId: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        viewModelScope.launch {
            try {
                val response = retrofitServiceForMoments.getMoment("Bearer $userToken",momentId)
                momentState = MomentState.Success(response.data)
                Log.d("getMoment", response.data.toString())
            } catch (e: Exception) {
                Log.e("Cart", "Failed to fetch cart", e)
                momentState = MomentState.Error
            }
        }
    }

    fun likeMoment(momentid: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    val response = retrofitServiceForMoments.postLikeToMoment("Bearer $userToken", momentid)
                    if(response.code!=0){
                        Log.e("点赞失败", response.msg)
                        Toast.makeText(getApplication(),"点赞失败: ${response.msg}，请你重新登录", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"点赞成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("点赞成功", response.msg)
                        //_refreshTrigger.tryEmit(Unit)
                        getMoment(limit = 100, offset = offset)
                        getMomentDetails(momentid)
                    }
                } catch (e: Exception) {
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    fun delikeMoment(momentid: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    val response = retrofitServiceForMoments.deleteLikeToMoment("Bearer $userToken", momentid)
                    if(response.code!=0){
                        Log.e("取消点赞失败", response.msg)
                        Toast.makeText(getApplication(),"取消点赞失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"取消点赞成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("取消点赞成功", response.msg)
                        getMoment(limit = 100, offset = offset)
                        getMomentDetails(momentid)
                    }
                } catch (e: Exception) {
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    fun likeReview(momentid: Int,momentrid: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    val response = retrofitServiceForMoments.postLikeToReview("Bearer $userToken", momentid,momentrid)
                    if(response.code!=0){
                        Log.e("点赞评论失败", response.msg)
                        Toast.makeText(getApplication(),"点赞评论失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"点赞评论成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("点赞评论成功", response.msg)
                        //刷新
                        getAllReview(momentid);
                    }
                } catch (e: Exception) {
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    fun delikeReview(momentid: Int,momentrid: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    val response = retrofitServiceForMoments.deleteLikeToReview("Bearer $userToken", momentid, momentrid)
                    if(response.code!=0){
                        Log.e("取消点赞评论失败", response.msg)
                        Toast.makeText(getApplication(),"取消点赞评论失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"取消点赞评论成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("取消点赞评论成功", response.msg)
                        //刷新
                        getAllReview(momentid);
                    }
                    // 处理响应
                } catch (e: Exception) {
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    fun getAllReview(momentId: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        viewModelScope.launch {
            try {
                val response = retrofitServiceForMoments.getReviews("Bearer $userToken",momentId,limit=10,offset=0)

                reviewState = ReviewState.Success(response.data)
            } catch (e: Exception) {
                Log.e("评论加载失败", "Failed to fetch cart", e)
                reviewState = ReviewState.Error
            }
        }
    }

    fun postMoment(requestBody: PostMomentRequest) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    val response = retrofitServiceForMoments.postMoment("Bearer $userToken",requestBody)
                    if(response.code!=0){
                        Log.e("发布失败", response.msg)
                        Toast.makeText(getApplication(),"发布失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"发布成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("发布成功", response.msg)
                        getMoment(limit = 100, offset = offset)
                    }
                } catch (e: Exception) {
                    Toast.makeText(getApplication(),"发布失败: $e", Toast.LENGTH_SHORT).show()
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    fun postReview(requestBody: PostReviewRequest,momentId: Int) {
        val userToken = userSessionManager.getUserSession()?.token // 从用户会话中获取token
        if (userToken != null) {
            viewModelScope.launch {
                try {
                    Log.d("post发的内容",requestBody.toString())
                    val response = retrofitServiceForMoments.postReviews("Bearer $userToken",requestBody,momentId)
                    if(response.code!=0){
                        Log.e("发布失败", response.msg)
                        Toast.makeText(getApplication(),"发布失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(),"发布成功: ${response.msg}", Toast.LENGTH_SHORT).show()
                        Log.e("发布成功", response.msg)
                        getAllReview(momentId);
                    }
                } catch (e: Exception) {
                    Log.e("LikeMoment", "Failed to like moment", e)
                }
            }
        }else{
            Toast.makeText(getApplication(),"请您登陆后再进行相关操作", Toast.LENGTH_SHORT).show()
        }
    }

    private val _navigateToDetails = MutableLiveData<Int?>()
    val navigateToDetails: LiveData<Int?> = _navigateToDetails

    fun resetNavigation() {
        _navigateToDetails.value = null
    }

    fun onMomentSelected(momentId: Int) {
        _navigateToDetails.value = momentId
    }
}
package com.example.marsphotos.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.marsphotos.model.Screen
import com.example.marsphotos.model.User
import com.example.marsphotos.model.request.LoginRequest
import com.example.marsphotos.model.request.RegisterRequest
import com.example.marsphotos.util.InterfaceApi
import com.example.marsphotos.util.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * UI state for the Home screen
 */

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _userSession = MutableStateFlow<User?>(null) // 初始值为null
    val userSession: StateFlow<User?> = _userSession.asStateFlow()

    init {
        // 初始化时，尝试从SharedPreferences加载用户会话
        val userSessionManager = UserSessionManager(application)
        _userSession.value = userSessionManager.getUserSession()
    }


    fun loginUser(bitid: String, password: String, navController: NavController) {
        viewModelScope.launch {
            try {
                val requestBody = LoginRequest(bitid, password)
                val response = InterfaceApi.retrofitServiceForUser.loginUser(requestBody)
                if (response.code == 0) {
                    val userData = User(
                        nickname = response.data!!.nickname,
                        avatar = response.data.avatar,
                        uid = response.data.uid,
                        token = response.data.token
                    )
                    // Use the application context to avoid leaks
                    UserSessionManager(getApplication()).saveUserSession(userData)
                    // Update the LiveData with the new user session
                    _userSession.value = userData
                    navController.navigate(Screen.UserMenu.route)
                } else {
                    Toast.makeText(getApplication(),"登录失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(),"登录失败", Toast.LENGTH_SHORT).show()
                Log.e("LoginUser", "Failed to login", e)
            }
        }
    }

    fun logoutUser() {
        // Use the application context to avoid leaks
        UserSessionManager(getApplication()).clearUserSession()
        // Update the LiveData to reflect that the user has logged out
        _userSession.value = null
    }

    fun registerUser(bitid: String, password: String, navController: NavController) {
        viewModelScope.launch {
            try {
                val requestBody = RegisterRequest(bitid, password)
                val response = InterfaceApi.retrofitServiceForUser.registerUser(requestBody)
                if (response.code == 0) {
                    // 注册成功的处理，保存用户会话
                    val userData = User(
                        nickname = response.data!!.nickname,
                        avatar = response.data.avatar,
                        uid = response.data.uid,
                        token = response.data.token
                    )
                    UserSessionManager(getApplication()).saveUserSession(userData)
                    _userSession.value = userData

                    Toast.makeText(getApplication(),"注册成功", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.UserMenu.route)
                } else {
                    // 注册失败的处理
                    Toast.makeText(getApplication(),"注册失败: ${response.msg}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(),"注册失败", Toast.LENGTH_SHORT).show()
                Log.e("RegisterUser", "Failed to register", e)
            }
        }
    }
}

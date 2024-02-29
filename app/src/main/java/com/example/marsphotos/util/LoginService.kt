package com.example.marsphotos.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.marsphotos.model.User

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val NICKNAME = "nickname"
        private const val AVATAR = "avatar"
        private const val UID = "uid"
        private const val TOKEN = "token"

        @Volatile private var INSTANCE: UserSessionManager? = null

        fun getInstance(context: Context): UserSessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserSessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveUserSession(userData: User) {
        try {
            prefs.edit().apply {
                putBoolean(IS_LOGGED_IN, true)
                putString(NICKNAME, userData.nickname)
                putString(AVATAR, userData.avatar)
                putInt(UID, userData.uid)
                putString(TOKEN, userData.token) // 保存token
                apply()
            }
        } catch (e: Exception) {
            Log.e("UserSessionManager", "Failed to save user session", e)
        }
    }


    fun getUserSession(): User? {
        if (!prefs.getBoolean(IS_LOGGED_IN, false)) {
            return null
        }
        return User(
            nickname = prefs.getString(NICKNAME, "") ?: "",
            avatar = prefs.getString(AVATAR, "") ?: "",
            uid = prefs.getInt(UID, -1),
            token = prefs.getString(TOKEN, null) // 获取token
        )
    }

    fun clearUserSession() {
        prefs.edit().clear().apply()
    }

    fun updateUserSession(userData: User) {
        if (isLoggedIn()) {
            saveUserSession(userData)
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }
}




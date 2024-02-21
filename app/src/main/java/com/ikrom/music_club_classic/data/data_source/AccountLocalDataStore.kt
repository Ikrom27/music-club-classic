package com.ikrom.music_club_classic.data.data_source

import android.content.Context

class AccountLocalDataStore(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val COOKIE_KEY = "cookie"
    }

    fun saveCookie(cookie: String) {
        val editor = sharedPreferences.edit()
        editor.putString(COOKIE_KEY, cookie)
        editor.apply()
    }

    fun getCookie(): String {
        return sharedPreferences.getString(COOKIE_KEY, null) ?: ""
    }
}

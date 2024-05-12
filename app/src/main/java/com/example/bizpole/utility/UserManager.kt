package com.example.bizpole.utility

import android.content.Context

object UserManager {
    private const val USER_PREFS_KEY = "user_prefs"
    private const val USER_ID_KEY = "user_id"

    fun getCurrentUser(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(USER_PREFS_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun saveCurrentUser(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(USER_PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(USER_ID_KEY, userId).apply()
    }
}
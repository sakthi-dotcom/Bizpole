package com.example.bizpole.utility

import android.content.Context

object FavoriteManager {
    private const val FAVORITE_PREF_KEY = "favorite_songs"

    fun getFavoriteSongs(context: Context, userId: String): Set<Long> {
        val sharedPreferences = context.getSharedPreferences(FAVORITE_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(userId, setOf())?.mapNotNull { it.toLongOrNull() }?.toSet() ?: emptySet()
    }

    fun addFavoriteSong(context: Context, userId: String, songId: Long) {
        val favoriteSongs = getFavoriteSongs(context, userId).toMutableSet()
        favoriteSongs.add(songId)
        saveFavoriteSongs(context, userId, favoriteSongs)
    }

    fun removeFavoriteSong(context: Context, userId: String, songId: Long) {
        val favoriteSongs = getFavoriteSongs(context, userId).toMutableSet()
        favoriteSongs.remove(songId)
        saveFavoriteSongs(context, userId, favoriteSongs)
    }

    private fun saveFavoriteSongs(context: Context, userId: String, favoriteSongs: Set<Long>) {
        val sharedPreferences = context.getSharedPreferences(FAVORITE_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putStringSet(userId, favoriteSongs.map { it.toString() }.toSet()).apply()
    }
}



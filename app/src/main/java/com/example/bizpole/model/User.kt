package com.example.bizpole.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val favoriteSongs: MutableList<Song> = mutableListOf()
)


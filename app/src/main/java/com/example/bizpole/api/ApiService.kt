package com.example.bizpole.api

import com.example.bizpole.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchSongs(@Query("term") term: String): SearchResult
}
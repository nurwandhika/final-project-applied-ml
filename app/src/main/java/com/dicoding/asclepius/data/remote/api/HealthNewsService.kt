package com.dicoding.asclepius.data.remote.api

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HealthNewsService {
    @GET("top-headlines")
    suspend fun getHealthRelatedArticles(
        @Query("q") searchQuery: String = "cancer",
        @Query("category") newsCategory: String = "health",
        @Query("language") newsLanguage: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.TOKEN,
    ): NewsResponse
}

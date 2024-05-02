package com.dicoding.asclepius.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("totalResults")
    val totalNewsCount: Int? = null,

    @field:SerializedName("articles")
    val newsList: List<NewsItem>,

    @field:SerializedName("status")
    val apiResponseStatus: String? = null
)
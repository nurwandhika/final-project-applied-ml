package com.dicoding.asclepius.data.model

import com.google.gson.annotations.SerializedName

data class NewsItem(
    @field:SerializedName("urlToImage")
    val newsImageUrl: String? = null,

    @field:SerializedName("description")
    val newsDescription: String? = null,

    @field:SerializedName("title")
    val newsTitle: String? = null,

    @field:SerializedName("url")
    val newsUrl: String? = null,
)
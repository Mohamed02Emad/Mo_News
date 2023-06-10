package com.androiddevs.mvvmnewsapp.data.models

import androidx.annotation.Keep

@Keep
data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
package com.androiddevs.mvvmnewsapp.data.models

import androidx.annotation.Keep

@Keep
data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
package com.androiddevs.mvvmnewsapp.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)
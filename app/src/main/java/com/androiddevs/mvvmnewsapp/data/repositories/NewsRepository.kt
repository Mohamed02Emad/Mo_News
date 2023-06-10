package com.androiddevs.mvvmnewsapp.data.repositories

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.data.db.ArticleDataBase
import com.androiddevs.mvvmnewsapp.data.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDataBase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int = 1): Response<NewsResponse> {
        return RetrofitInstance.api.getHeadLineNews(countryCode = countryCode , pageNumber = pageNumber)
    }

}
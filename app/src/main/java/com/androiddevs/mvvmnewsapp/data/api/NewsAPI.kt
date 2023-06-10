package com.androiddevs.mvvmnewsapp.data.api

import com.androiddevs.mvvmnewsapp.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    //the documentation to get the parameters  https://newsapi.org/docs
    // i have an api key in a git ignored file

    @GET(getHeadLines)
    suspend fun getHeadLineNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("country")
        countryCode: String = "eg",
        @Query("page")
        pageNumber: Int = 1,
    ):Response<NewsResponse>

    @GET(getEverything)
    suspend fun searchForNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
    ):Response<NewsResponse>
}
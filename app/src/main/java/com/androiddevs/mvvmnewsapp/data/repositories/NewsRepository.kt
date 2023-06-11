package com.androiddevs.mvvmnewsapp.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.androiddevs.mvvmnewsapp.data.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.data.db.ArticleDataBase
import com.androiddevs.mvvmnewsapp.data.models.Article
import com.androiddevs.mvvmnewsapp.data.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDataBase,val context: Context) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int = 1): Response<NewsResponse> {
        return RetrofitInstance.api.getHeadLineNews(
            countryCode = countryCode,
            pageNumber = pageNumber
        )
    }

    suspend fun searchForNews(query: String, pageNumber: Int = 1): Response<NewsResponse> {
        return RetrofitInstance.api.searchForNews(searchQuery = query, pageNumber = pageNumber)
    }

    suspend fun updateAndInsert(article: Article): Long {
        db.getArticleDao().updateAndInsert(article)
        return article.id.toLong()
    }

    fun getAllArticles(): LiveData<List<Article>> {
        return db.getArticleDao().getAllArticles()
    }

    suspend fun deleteArticle(article: Article) {
        db.getArticleDao().delete(article)
    }

}
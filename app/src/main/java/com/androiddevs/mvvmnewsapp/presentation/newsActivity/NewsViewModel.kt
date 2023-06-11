package com.androiddevs.mvvmnewsapp.presentation.newsActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.api.Resource
import com.androiddevs.mvvmnewsapp.data.models.Article
import com.androiddevs.mvvmnewsapp.data.models.NewsResponse
import com.androiddevs.mvvmnewsapp.data.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val repository: NewsRepository
) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingNewsResponse : NewsResponse? = null
    var breakingNewsPage: Int = 1

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsResponse : NewsResponse? = null
    var searchNewsPage: Int = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchForNews(query: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = repository.searchForNews(query, searchNewsPage)
        _searchNews.postValue(handleSearchForNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = result
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resource.Sucess(breakingNewsResponse?:result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchForNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = result
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resource.Sucess(searchNewsResponse?:result)
            }
        }
        return Resource.Error(response.message())
    }

     fun insertAndUpdateArticle(article: Article) = viewModelScope.launch { repository.updateAndInsert(article)}

     fun getAllArticles() = repository.getAllArticles()

     fun deleteArticle(article: Article) = viewModelScope.launch { repository.deleteArticle(article)}


}
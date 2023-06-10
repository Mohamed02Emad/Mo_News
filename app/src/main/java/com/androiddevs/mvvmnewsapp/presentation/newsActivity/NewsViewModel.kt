package com.androiddevs.mvvmnewsapp.presentation.newsActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.api.Resource
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
    var breakingNewsPage: Int = 1

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsPage: Int = 1

    init {
        getBreakingNews("us",1)
    }
     fun getBreakingNews(countryCode: String, pageNumber: Int) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, pageNumber)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }
     fun searchForNews(query: String, pageNumber: Int) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = repository.searchForNews(query, pageNumber)
        _searchNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
             response.body()?.let { result ->
             return  Resource.Sucess(result)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchForNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
             response.body()?.let { result ->
             return  Resource.Sucess(result)
            }
        }
        return Resource.Error(response.message())
    }

}
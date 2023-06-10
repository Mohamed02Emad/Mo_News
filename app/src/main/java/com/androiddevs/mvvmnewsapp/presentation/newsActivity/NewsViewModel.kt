package com.androiddevs.mvvmnewsapp.presentation.newsActivity

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.data.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val repository: NewsRepository
) : ViewModel() {

}
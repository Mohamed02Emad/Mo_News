package com.androiddevs.mvvmnewsapp.api

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Sucess<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String?) : Resource<T>( message = message)
    class Loading<T>() : Resource<T>()
}
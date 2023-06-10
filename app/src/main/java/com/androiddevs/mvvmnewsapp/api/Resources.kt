package com.androiddevs.mvvmnewsapp.api

sealed class Resources<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Sucess<T>(data: T?) : Resources<T>(data)
    class Error<T>(message: String?, data: T?) : Resources<T>(data, message)
    class Loading<T>() : Resources<T>()
}
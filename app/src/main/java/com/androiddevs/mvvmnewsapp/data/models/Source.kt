package com.androiddevs.mvvmnewsapp.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Source(
    val id: String,
    val name: String
):Parcelable
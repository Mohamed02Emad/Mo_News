package com.androiddevs.mvvmnewsapp.data.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.data.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toSource(string: String):Source{
        return Source(string,string)
    }
}
package com.androiddevs.mvvmnewsapp.di

import android.content.Context
import com.androiddevs.mvvmnewsapp.data.db.ArticleDao
import com.androiddevs.mvvmnewsapp.data.db.ArticleDataBase
import com.androiddevs.mvvmnewsapp.data.repositories.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object module {


    @Singleton
    @Provides
    fun provideNewsRepository(
        articleDataBase: ArticleDataBase,
    ): NewsRepository = NewsRepository(articleDataBase)

    @Provides
    fun provideNewsDao(articleDataBase: ArticleDataBase): ArticleDao {
        return articleDataBase.getArticleDao()
    }

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext appContext: Context): ArticleDataBase {
        return ArticleDataBase.invoke(appContext)
    }
}
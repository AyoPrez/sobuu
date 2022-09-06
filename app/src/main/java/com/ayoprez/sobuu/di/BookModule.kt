package com.ayoprez.sobuu.di

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.book.database.BookLocalDataImpl
import com.ayoprez.sobuu.shared.features.book.database.IBookLocalData
import com.ayoprez.sobuu.shared.features.book.remote.BookApi
import com.ayoprez.sobuu.shared.features.book.remote.BookRemoteDataImpl
import com.ayoprez.sobuu.shared.features.book.remote.IBookRemoteData
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.features.book.repository.IBookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object BookModule {
    @Provides
    fun provideBookApi(retrofitBuilder: Retrofit.Builder): BookApi = retrofitBuilder
        .build()
        .create(BookApi::class.java)

    @Provides
    fun provideBookRemoteData(bookApi: BookApi): IBookRemoteData = BookRemoteDataImpl(bookApi)

    @Provides
    fun provideBookLocalData(prefs: SharedPreferences): IBookLocalData = BookLocalDataImpl(prefs)

    @Provides
    fun provideBookRepository(
        bookRemoteData: IBookRemoteData,
        bookLocalData: IBookLocalData
    ): IBookRepository = BookRepositoryImpl(
        bookRemoteData = bookRemoteData,
        bookLocalData = bookLocalData)

}
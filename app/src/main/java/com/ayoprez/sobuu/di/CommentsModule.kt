package com.ayoprez.sobuu.di

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.comments.database.CommentLocalDataImpl
import com.ayoprez.sobuu.shared.features.comments.database.ICommentLocalData
import com.ayoprez.sobuu.shared.features.comments.remote.CommentApi
import com.ayoprez.sobuu.shared.features.comments.remote.CommentRemoteDataImpl
import com.ayoprez.sobuu.shared.features.comments.remote.ICommentRemoteData
import com.ayoprez.sobuu.shared.features.comments.repository.CommentRepositoryImpl
import com.ayoprez.sobuu.shared.features.comments.repository.ICommentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object CommentsModule {

    @Provides
    fun provideCommentApi(retrofitBuilder: Retrofit.Builder): CommentApi = retrofitBuilder
        .build()
        .create(CommentApi::class.java)

    @Provides
    fun provideCommentRemoteData(commentApi: CommentApi): ICommentRemoteData = CommentRemoteDataImpl(commentApi)

    @Provides
    fun provideCommentLocalData(prefs: SharedPreferences): ICommentLocalData = CommentLocalDataImpl(prefs)

    @Provides
    fun provideCommentRepository(
        commentRemoteData: ICommentRemoteData,
        commentLocalData: ICommentLocalData
    ): ICommentRepository = CommentRepositoryImpl(
        commentRemoteData = commentRemoteData,
        commentLocalData = commentLocalData)
}
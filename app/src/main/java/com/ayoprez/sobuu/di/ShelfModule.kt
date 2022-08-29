package com.ayoprez.sobuu.di

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.shelf.database.IShelfLocalData
import com.ayoprez.sobuu.shared.features.shelf.database.ShelfLocalDataImpl
import com.ayoprez.sobuu.shared.features.shelf.remote.IShelfRemoteData
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfApi
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfRemoteDataImpl
import com.ayoprez.sobuu.shared.features.shelf.repository.IShelfRepository
import com.ayoprez.sobuu.shared.features.shelf.repository.ShelfRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ShelfModule {

    @Provides
    fun provideShelfApi(retrofitBuilder: Retrofit.Builder): ShelfApi = retrofitBuilder
        .build()
        .create(ShelfApi::class.java)

    @Provides
    fun provideShelfRemoteData(shelfApi: ShelfApi): IShelfRemoteData = ShelfRemoteDataImpl(shelfApi)

    @Provides
    fun provideShelfLocalData(prefs: SharedPreferences): IShelfLocalData = ShelfLocalDataImpl(prefs)

    @Provides
    fun provideShelfRepository(
        shelfRemoteData: IShelfRemoteData,
        shelfLocalData: IShelfLocalData
    ): IShelfRepository = ShelfRepositoryImpl(
        shelfRemoteData = shelfRemoteData,
        shelfLocalData = shelfLocalData)
}
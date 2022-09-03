package com.ayoprez.sobuu.di

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.profile.database.IProfileLocalData
import com.ayoprez.sobuu.shared.features.profile.database.ProfileLocalDataImpl
import com.ayoprez.sobuu.shared.features.profile.remote.IProfileRemoteData
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileApi
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileRemoteDataImpl
import com.ayoprez.sobuu.shared.features.profile.repository.IProfileRepository
import com.ayoprez.sobuu.shared.features.profile.repository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    fun provideProfileApi(retrofitBuilder: Retrofit.Builder): ProfileApi = retrofitBuilder
        .build()
        .create(ProfileApi::class.java)

    @Provides
    fun provideProfileRemoteData(profileApi: ProfileApi): IProfileRemoteData = ProfileRemoteDataImpl(profileApi)

    @Provides
    fun provideProfileLocalData(prefs: SharedPreferences): IProfileLocalData = ProfileLocalDataImpl(prefs)

    @Provides
    fun provideProfileRepository(
        profileRemoteData: IProfileRemoteData,
        profileLocalData: IProfileLocalData
    ): IProfileRepository = ProfileRepositoryImpl(
        profileRemoteData = profileRemoteData,
        profileLocalData = profileLocalData)
}
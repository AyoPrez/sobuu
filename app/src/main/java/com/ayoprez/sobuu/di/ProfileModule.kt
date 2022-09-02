package com.ayoprez.sobuu.di

import com.ayoprez.sobuu.shared.features.profile.remote.IProfileRemoteData
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileApi
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileRemoteDataImpl
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

    //@Provides
    //fun provideProfileLocalData(prefs: SharedPreferences): IProfileLocalData = ShelfLocalDataImpl(prefs)

    /*@Provides
    fun provideProfileRepository(
        profileRemoteData: IProfileRemoteData,
        profileLocalData: IProfileLocalData
    ): IShelfRepository = ShelfRepositoryImpl(
        profileRemoteData = profileRemoteData,
        profileLocalData = profileLocalData)*/
}
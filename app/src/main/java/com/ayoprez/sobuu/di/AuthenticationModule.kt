package com.ayoprez.sobuu.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.authentication.database.AuthenticationLocalDataImpl
import com.ayoprez.sobuu.shared.features.authentication.database.IAuthenticationLocalData
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationApi
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationRemoteDataImpl
import com.ayoprez.sobuu.shared.features.authentication.remote.IAuthenticationRemoteData
import com.ayoprez.sobuu.shared.features.authentication.repository.AuthenticationRepositoryImpl
import com.ayoprez.sobuu.shared.features.authentication.repository.IAuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    fun provideAuthenticationApi(retrofitBuilder: Retrofit.Builder): AuthenticationApi = retrofitBuilder
        .build()
        .create(AuthenticationApi::class.java)

    @Provides
    fun provideAuthenticationRemoteData(authApi: AuthenticationApi): IAuthenticationRemoteData {
        return AuthenticationRemoteDataImpl(authApi)
    }

    @Provides
    fun provideAuthenticationLocalData(prefs: SharedPreferences): IAuthenticationLocalData {
        return AuthenticationLocalDataImpl(prefs)
    }

    @Provides
    fun provideAuthenticationRepository(
        authRemoteData: IAuthenticationRemoteData,
        authLocalData: IAuthenticationLocalData
    ): IAuthenticationRepository {
        return AuthenticationRepositoryImpl(authRemoteData, authLocalData)
    }

    @Provides
    fun provideSharePreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("com.ayoprez.sobuu.prefs", Context.MODE_PRIVATE)
    }
}
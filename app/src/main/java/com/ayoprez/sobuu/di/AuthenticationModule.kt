package com.ayoprez.sobuu.di

import android.app.Application
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import com.ayoprez.sobuu.BuildConfig
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
    fun provideEncryptedSharePreferences(app: Application): SharedPreferences {
        val spec = KeyGenParameterSpec.Builder(
            BuildConfig.MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKey = MasterKey.Builder(app)
            .setKeyGenParameterSpec(spec)
            .build()

        return EncryptedSharedPreferences.create(
            app.baseContext,
            "com.ayoprez.sobuu.prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
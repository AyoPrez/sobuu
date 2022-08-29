package com.ayoprez.sobuu.di

import com.ayoprez.sobuu.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(baseUrl)

    @Provides
    fun provideParseAuthInterceptor(): Interceptor {
        class ParseAuthInterceptor: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()

                val builder: Request.Builder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("X-Parse-Application-Id", BuildConfig.APP_ID)
                    .header("X-Parse-REST-API-Key", BuildConfig.API_KEY)

                val request: Request = builder.build()
                return chain.proceed(request)
            }
        }

        return ParseAuthInterceptor()
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            okHttpClient.addInterceptor(logging)
        }

        okHttpClient.addInterceptor(authInterceptor)

        return okHttpClient.build()
    }

    @Provides
    fun provideBaseUrl(): String {
        return "https://parseapi.back4app.com/"
    }

}
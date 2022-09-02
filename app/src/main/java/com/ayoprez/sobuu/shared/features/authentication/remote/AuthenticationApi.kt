package com.ayoprez.sobuu.shared.features.authentication.remote

import retrofit2.Response
import retrofit2.http.*

interface AuthenticationApi {

    @FormUrlEncoded
    @POST("functions/getSessionToken")
    suspend fun getSessionToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<SessionTokenApi>

    @FormUrlEncoded
    @POST("functions/signUp")
    suspend fun signUp(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
    ): Response<Void>

    @GET("parse/users/me")
    suspend fun authenticate(
        @Header("X-Parse-Session-Token") sessionToken: String,
    ): Response<SessionTokenApi>

    @FormUrlEncoded
    @POST("functions/logout")
    suspend fun logout(
        @Field("sessionToken") sessionToken: String,
    ): Response<Void>

    @FormUrlEncoded
    @POST("functions/resetPassword")
    suspend fun resetPassword(
        @Field("email") email: String,
    ): Response<Void>
}
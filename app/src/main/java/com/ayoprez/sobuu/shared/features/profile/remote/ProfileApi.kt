package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.Profile
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileApi {

    @FormUrlEncoded
    @POST("functions/getUserProfile")
    suspend fun getUserProfile(
        @Field("sessionToken") sessionToken: String,
    ): Response<Profile>

}
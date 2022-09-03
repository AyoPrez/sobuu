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

    @FormUrlEncoded
    @POST("functions/getProfileFromId")
    suspend fun getProfileFromId(
        @Field("sessionToken") sessionToken: String,
        @Field("profileId") profileId: String,
    ): Response<Profile>

    @FormUrlEncoded
    @POST("functions/followProfile")
    suspend fun followProfile(
        @Field("sessionToken") sessionToken: String,
        @Field("profileId") profileId: String,
    ): Response<Profile>

    @FormUrlEncoded
    @POST("functions/unfollowProfile")
    suspend fun unfollowProfile(
        @Field("sessionToken") sessionToken: String,
        @Field("profileId") profileId: String,
    ): Response<Profile>

    @FormUrlEncoded
    @POST("functions/getFollowingProfiles")
    suspend fun getFollowingProfiles(
        @Field("sessionToken") sessionToken: String,
    ): Response<List<Profile>>
}
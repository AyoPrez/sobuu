package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.Profile

interface IProfileRemoteData {
    suspend fun getUserProfile(sessionToken: String?): ProfileResult<Profile>

    suspend fun getProfileFromId(sessionToken: String?, profileId: String): ProfileResult<Profile>

    suspend fun followProfile(sessionToken: String?, profileId: String): ProfileResult<Profile>

    suspend fun unfollowProfile(sessionToken: String?, profileId: String): ProfileResult<Profile>

    suspend fun getFollowingProfiles(sessionToken: String?): ProfileResult<List<Profile>>
}
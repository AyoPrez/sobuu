package com.ayoprez.sobuu.shared.features.profile.repository

import com.ayoprez.sobuu.shared.features.profile.remote.ProfileResult
import com.ayoprez.sobuu.shared.models.bo_models.Profile

interface IProfileRepository {
    suspend fun getUserProfile(): ProfileResult<Profile>

    suspend fun getProfileFromId(profileId: String): ProfileResult<Profile>

    suspend fun followProfile(profileId: String): ProfileResult<Profile>

    suspend fun unfollowProfile(profileId: String): ProfileResult<Profile>

    suspend fun getFollowingProfiles(): ProfileResult<List<Profile>>
}
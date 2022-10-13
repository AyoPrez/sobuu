package com.ayoprez.sobuu.shared.features.profile.repository

import com.ayoprez.sobuu.shared.features.profile.database.IProfileLocalData
import com.ayoprez.sobuu.shared.features.profile.remote.IProfileRemoteData
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileError
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileResult
import com.ayoprez.sobuu.shared.models.bo_models.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteData: IProfileRemoteData,
    private val profileLocalData: IProfileLocalData
) : IProfileRepository {

    override suspend fun getUserProfile(): ProfileResult<Profile> = execute {
        profileRemoteData.getUserProfile(
            sessionToken = it,
        )
    }

    override suspend fun getProfileFromId(profileId: String): ProfileResult<Profile> = execute {
        profileRemoteData.getProfileFromId(
            sessionToken = it,
            profileId = profileId,
        )
    }

    override suspend fun followProfile(profileId: String): ProfileResult<Profile> = execute {
        profileRemoteData.followProfile(
            sessionToken = it,
            profileId = profileId,
        )
    }

    override suspend fun unfollowProfile(profileId: String): ProfileResult<Profile> = execute {
        profileRemoteData.unfollowProfile(
           sessionToken = it,
           profileId = profileId,
        )
    }

    override suspend fun getFollowingProfiles(): ProfileResult<List<Profile>> = execute {
        profileRemoteData.getFollowingProfiles(
            sessionToken = it,
        )
    }

    private suspend fun <T>execute(func: suspend (sessionToken: String) -> ProfileResult<T>) : ProfileResult<T> {
        val sessionToken = profileLocalData.getSessionToken() ?: return ProfileResult.Error(
            ProfileError.InvalidSessionTokenError)
        return func(sessionToken)
    }
}
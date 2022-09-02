package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.Profile
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDataImpl @Inject constructor(
    private val api: ProfileApi
): IProfileRemoteData {

    override suspend fun getUserProfile(sessionToken: String?): ProfileResult<Profile> = execute(sessionToken) {
        api.getUserProfile(
            sessionToken = it,
        )
    }

    override suspend fun getProfileFromId(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun followProfile(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun unfollowProfile(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowingProfiles(sessionToken: String?): ProfileResult<List<Profile>> {
        TODO("Not yet implemented")
    }

    private suspend fun <T>execute(sessionToken: String?, func: suspend (sessionToken: String) -> Response<T>): ProfileResult<T> {
        return try {
            if (sessionToken.isNullOrBlank()) return ProfileResult.Error(ProfileError.InvalidSessionTokenError)

            val result = func(sessionToken)

            if (result.body() == null && result.errorBody() != null) return handleResponseError(
                result.errorBody()
            )

            return ProfileResult.Success(data = result.body())
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> ProfileResult.Error(ProfileError.UnauthorizedQueryError)
                209 -> ProfileResult.Error(ProfileError.InvalidSessionTokenError)
                else -> ProfileResult.Error(ProfileError.UnknownError)
            }
        } catch (e: Exception) {
            ProfileResult.Error(ProfileError.UnknownError)
        }
    }

    private fun <T>handleResponseError(errorBody: ResponseBody?): ProfileResult<T> {
        if (errorBody == null) return ProfileResult.Error(error = ProfileError.UnknownError)
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when(it) {
                    101 -> ProfileResult.Error(ProfileError.UnauthorizedQueryError)
                    141 -> ProfileResult.Error(ProfileError.ProcessingQueryError)
                    124 -> ProfileResult.Error(ProfileError.TimeOutError)
                    209 -> ProfileResult.Error(ProfileError.InvalidSessionTokenError)
                    else -> ProfileResult.Error(ProfileError.UnknownError)
                }
            }
    }
}
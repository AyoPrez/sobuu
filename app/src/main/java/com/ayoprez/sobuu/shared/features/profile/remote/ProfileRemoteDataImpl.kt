package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.BookProgress
import com.ayoprez.sobuu.shared.models.Profile
import com.ayoprez.sobuu.shared.models.Shelf
import com.ayoprez.sobuu.shared.models.api_models.GetUserProfile
import com.ayoprez.sobuu.shared.models.api_models.UserShelf
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import com.ayoprez.sobuu.shared.models.api_models.BookProgress as bpApiModel

class ProfileRemoteDataImpl @Inject constructor(
    private val api: ProfileApi
): IProfileRemoteData {

    override suspend fun getUserProfile(sessionToken: String?): ProfileResult<Profile> {
        val result = execute(sessionToken) {
            api.getUserProfile(
                sessionToken = it,
            )
        }

        return if(result.data != null) {
            ProfileResult.Success(data = result.data.toProfile())
        } else {
            ProfileResult.Error(error = result.error)
        }
    }

    override suspend fun getProfileFromId(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        if (profileId.isBlank()) return ProfileResult.Error(ProfileError.InvalidProfileIdError)
        return execute(sessionToken) {
            api.getProfileFromId(it, profileId)
        }
    }

    override suspend fun followProfile(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        if (profileId.isBlank()) return ProfileResult.Error(ProfileError.InvalidProfileIdError)
        return execute(sessionToken) {
            api.followProfile(it, profileId)
        }
    }

    override suspend fun unfollowProfile(
        sessionToken: String?,
        profileId: String
    ): ProfileResult<Profile> {
        if (profileId.isBlank()) return ProfileResult.Error(ProfileError.InvalidProfileIdError)
        return execute(sessionToken) {
            api.unfollowProfile(it, profileId)
        }
    }

    override suspend fun getFollowingProfiles(sessionToken: String?): ProfileResult<List<Profile>> = execute(sessionToken) {
        api.getFollowingProfiles(sessionToken = it)
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

    private fun GetUserProfile.toProfile(): Profile {
        return Profile(
            id = this.result.id,
            firstName = this.result.firstName,
            lastName = this.result.lastName,
            following = emptyList(),
            userShelves = this.result.userShelves.toShelfList(),
            bookProgress = this.result.bookProgress.toBookProgressList(),
            giveUp = emptyList(),
            alreadyRead = emptyList()
        )
    }

    private fun List<GetUserProfile>.toProfileList(): List<Profile> {
        return this.map {
            it.toProfile()
        }
    }

    private fun UserShelf.toShelf(): Shelf {
        return Shelf(
            id = this.id,
            books = emptyList(),
            name = this.name,
            description = this.description ?: "",
            isPublic = this.isPublic,
        )
    }

    private fun List<UserShelf>.toShelfList(): List<Shelf> {
        return this.map {
            it.toShelf()
        }
    }

    private fun bpApiModel.toBookProgress(): BookProgress {
        return BookProgress(
            id = this.id,
            percentage = this.percentage,
            page = this.page,
            finished = this.finished,
            giveUp = this.giveUp,
        )
    }

    private fun List<bpApiModel>.toBookProgressList(): List<BookProgress> {
        return this.map {
            it.toBookProgress()
        }
    }
}
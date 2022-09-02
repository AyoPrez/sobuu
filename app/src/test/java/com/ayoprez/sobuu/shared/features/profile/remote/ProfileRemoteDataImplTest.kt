package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.Profile
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

internal class ProfileRemoteDataImplTest {

    private lateinit var profileRemote: IProfileRemoteData
    private val profile: Profile = Profile(
        id = "ds90ohw",
        firstName = "Glock",
        lastName = "Stephen",
        giveUp = listOf(),
        alreadyRead = listOf(),
        userShelves = listOf(),
        following = listOf(),
    )

    @MockK
    lateinit var profileApi: ProfileApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        profileRemote = ProfileRemoteDataImpl(profileApi)
    }

    //region Get User Profile
    @Test
    fun `Get User Profile - Everything good`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getUserProfile("9hosnd9")
        }

        assertEquals(result.data?.firstName, "Glock")
    }


    @Test
    fun `Get User Profile - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getUserProfile("")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get User Profile - If session token is null, should return invalid session token error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getUserProfile(null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get User Profile - If request return 141 error, should return processing query error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getUserProfile("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get User Profile - If request return 124 error, should return time out error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getUserProfile("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get User Profile - If request return 101 error, should return unauthorized query error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getUserProfile("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get Profile From Id

    //endregion

    //region follow Profile

    //endregion

    //region Unfollow Profile

    //endregion

    //region Get Following Profiles

    //endregion

}
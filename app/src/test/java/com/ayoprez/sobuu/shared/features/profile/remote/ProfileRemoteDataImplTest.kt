package com.ayoprez.sobuu.shared.features.profile.remote

import com.ayoprez.sobuu.shared.models.api_models.GetUserProfile
import com.ayoprez.sobuu.shared.models.api_models.Result
import com.ayoprez.sobuu.shared.models.bo_models.Profile
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

    private val getUserProfile = GetUserProfile(
        result = Result(
            id = "ds90ohw",
            bookProgress = listOf(),
            firstName = "Glock",
            lastName = "Stephen",
            userShelves = listOf(),
        )
    )
    private val profile: Profile = Profile(
        id = "ds90ohw",
        firstName = "Glock",
        lastName = "Stephen",
        giveUp = listOf(),
        alreadyRead = listOf(),
        userShelves = listOf(),
        following = listOf(),
        bookProgress = listOf(),
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
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(getUserProfile)

        val result = runBlocking {
            profileRemote.getUserProfile("9hosnd9")
        }

        assertEquals(result.data?.firstName, "Glock")
    }

    @Test
    fun `Get User Profile - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(getUserProfile)

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
        coEvery { profileApi.getUserProfile(any()) } returns Response.success(getUserProfile)

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
    @Test
    fun `Get Profile from id - Everything good`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getProfileFromId("9hosnd9", "qas98hunewdso8")
        }

        assertEquals(result.data?.firstName, "Glock")
    }

    @Test
    fun `Get Profile from id - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getProfileFromId("", "98uwieio")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get Profile from id - If session token is null, should return invalid session token error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getProfileFromId(null, "qw98oiks")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get Profile from id - If profile id is empty, should return invalid profile id error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.getProfileFromId("98iunwe98", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidProfileIdError::class.java)
        )
    }

    @Test
    fun `Get Profile from id - If request return 141 error, should return processing query error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getProfileFromId("sp8hsh908", "98ewoi9")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get Profile from id - If request return 124 error, should return time out error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getProfileFromId("sp8hsh908", "a8wes98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get Profile from id - If request return 101 error, should return unauthorized query error`() {
        coEvery { profileApi.getProfileFromId(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getProfileFromId("sp8hsh908", "q8hn34ew98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region follow Profile
    @Test
    fun `Follow profile - Everything good`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.followProfile("9hosnd9", "qas98hunewdso8")
        }

        assertEquals(result.data?.firstName, "Glock")
    }

    @Test
    fun `Follow profile - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.followProfile("", "98uwieio")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Follow profile - If session token is null, should return invalid session token error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.followProfile(null, "qw98oiks")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Follow profile - If profile id is empty, should return invalid profile id error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.followProfile("98iunwe98", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidProfileIdError::class.java)
        )
    }

    @Test
    fun `Follow profile - If request return 141 error, should return processing query error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.followProfile("sp8hsh908", "98ewoi9")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Follow profile - If request return 124 error, should return time out error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.followProfile("sp8hsh908", "a8wes98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Follow profile - If request return 101 error, should return unauthorized query error`() {
        coEvery { profileApi.followProfile(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.followProfile("sp8hsh908", "q8hn34ew98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Unfollow Profile
    @Test
    fun `Unfollow profile - Everything good`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.unfollowProfile("9hosnd9", "qas98hunewdso8")
        }

        assertEquals(result.data?.firstName, "Glock")
    }

    @Test
    fun `Unfollow profile - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.unfollowProfile("", "98uwieio")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Unfollow profile - If session token is null, should return invalid session token error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.unfollowProfile(null, "qw98oiks")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Unfollow profile - If profile id is empty, should return invalid profile id error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.success(profile)

        val result = runBlocking {
            profileRemote.unfollowProfile("98iunwe98", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidProfileIdError::class.java)
        )
    }

    @Test
    fun `Unfollow profile - If request return 141 error, should return processing query error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.unfollowProfile("sp8hsh908", "98ewoi9")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Unfollow profile - If request return 124 error, should return time out error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.unfollowProfile("sp8hsh908", "a8wes98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Unfollow profile - If request return 101 error, should return unauthorized query error`() {
        coEvery { profileApi.unfollowProfile(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.unfollowProfile("sp8hsh908", "q8hn34ew98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get Following Profiles
    @Test
    fun `Get following profiles - Everything good`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.success(listOf(profile))

        val result = runBlocking {
            profileRemote.getFollowingProfiles("9hosnd9")
        }

        assertEquals(result.data?.get(0)?.firstName, "Glock")
    }

    @Test
    fun `Get following profiles - If session token is empty, should return invalid session token error`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.success(listOf(profile))

        val result = runBlocking {
            profileRemote.getFollowingProfiles("")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get following profiles - If session token is null, should return invalid session token error`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.success(listOf(profile))

        val result = runBlocking {
            profileRemote.getFollowingProfiles(null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get following profiles - If request return 141 error, should return processing query error`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getFollowingProfiles("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get following profiles - If request return 124 error, should return time out error`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getFollowingProfiles("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get following profiles - If request return 101 error, should return unauthorized query error`() {
        coEvery { profileApi.getFollowingProfiles(any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            profileRemote.getFollowingProfiles("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(ProfileError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

}
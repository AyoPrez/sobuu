package com.ayoprez.sobuu.shared.features.profile.repository

import com.ayoprez.sobuu.shared.features.profile.database.IProfileLocalData
import com.ayoprez.sobuu.shared.features.profile.remote.IProfileRemoteData
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileError
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test

internal class ProfileRepositoryImplTest {
    private lateinit var profileRepo: IProfileRepository

    @MockK
    lateinit var remoteData: IProfileRemoteData

    @MockK
    lateinit var localData: IProfileLocalData

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        profileRepo = ProfileRepositoryImpl(profileRemoteData = remoteData, profileLocalData = localData)
    }

    //region Get user profile
    @Test
    fun `Get user profile - Everything good`() {
        coEvery { remoteData.getUserProfile(any()) } returns ProfileResult.Success()
        coEvery { localData.getSessionToken() } returns "TestToken"

        val result = runBlocking {
            profileRepo.getUserProfile()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(ProfileResult.Success::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Get user profile - Empty session token, should return error`() {
        coEvery { remoteData.getUserProfile(any()) } returns ProfileResult.Error(error = ProfileError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            profileRepo.getUserProfile()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(ProfileResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Get user profile - Null session token, should return error`() {
        coEvery { remoteData.getUserProfile(any()) } returns ProfileResult.Error(error = ProfileError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            profileRepo.getUserProfile()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(ProfileResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }
    //endregion

    //region Get profile from Id

    //endregion

    //region Follow profile

    //endregion

    //region Unfollow profile

    //endregion

    //region Get following profiles

    //endregion
}
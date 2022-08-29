package com.ayoprez.sobuu.shared.features.authentication.repository

import com.ayoprez.sobuu.shared.features.authentication.database.IAuthenticationLocalData
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.shared.features.authentication.remote.IAuthenticationRemoteData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

internal class AuthenticationRepositoryImplTest {

    private lateinit var authRepo: IAuthenticationRepository

    @MockK
    lateinit var remoteData: IAuthenticationRemoteData

    @MockK
    lateinit var localData: IAuthenticationLocalData

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        authRepo = AuthenticationRepositoryImpl(authRemoteData = remoteData, authLocalData = localData)
    }

    //region Login
    @Test
    fun `Login - Not null`() {
        coEvery { remoteData.login(any(), any()) } returns AuthenticationResult.Authorized("TestSessionToken")

        val result = runBlocking {
            authRepo.loginUser("test", "test")
        }

        assertThat(result, instanceOf(AuthenticationResult.Authorized::class.java))
        coVerify { localData.setSessionToken("TestSessionToken") }
    }

    @Test
    fun `Login - Empty credentials`() {
        coEvery { remoteData.login(any(), any()) } returns AuthenticationResult.Error(AuthenticationError.EmptyCredentialsError)

        val result = runBlocking {
            authRepo.loginUser("", "")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmptyCredentialsError::class.java))
        coVerify(exactly = 0) { localData.setSessionToken("") }
    }
    //endregion

    //region Authenticate
    @Test
    fun `Authenticate - Not null`() {
        coEvery { remoteData.authenticate(any()) } returns AuthenticationResult.Authorized()
        coEvery { localData.getSessionToken() } returns "s89hwfwf2d"

        val result = runBlocking {
            authRepo.authenticate()
        }

        assertThat(result, instanceOf(AuthenticationResult.Authorized::class.java))
        coVerify(exactly = 1) { localData.getSessionToken() }
        coVerify(exactly = 0) { localData.setSessionToken(null) }
    }

    @Test
    fun `Authenticate - If local stored session token is null, return unauthorized`() {
        coEvery { remoteData.authenticate(any()) } returns AuthenticationResult.Authorized()
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            authRepo.authenticate()
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
        coVerify(exactly = 1) { localData.getSessionToken() }
        coVerify(exactly = 0) { localData.setSessionToken(null) }
    }

    @Test
    fun `Authenticate - If session token is empty, return unauthorized`() {
        coEvery { remoteData.authenticate(any()) } returns AuthenticationResult.Unauthorized()
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            authRepo.authenticate()
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
        coVerify(exactly = 1) { localData.getSessionToken() }
        coVerify(exactly = 0) { localData.setSessionToken(null) }
    }

    @Test
    fun `Authenticate - If request response that session is invalid, return invalid session`() {
        coEvery { remoteData.authenticate(any()) } returns AuthenticationResult.Error(AuthenticationError.InvalidSessionToken)
        coEvery { localData.getSessionToken() } returns "oiskls98sbja9"

        val result = runBlocking {
            authRepo.authenticate()
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
        coVerify(exactly = 1) { localData.getSessionToken() }
        coVerify(exactly = 1) { localData.setSessionToken(null) }
    }
    //endregion
}
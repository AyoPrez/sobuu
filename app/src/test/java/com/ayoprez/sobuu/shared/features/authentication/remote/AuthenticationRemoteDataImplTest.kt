package com.ayoprez.sobuu.shared.features.authentication.remote

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

internal class AuthenticationRemoteDataImplTest {

    private lateinit var auth: IAuthenticationRemoteData

    @MockK
    lateinit var authApi: AuthenticationApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        auth = AuthenticationRemoteDataImpl(authApi)
    }

    //region Login
    @Test
    fun `Login - Not null`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.success(SessionTokenApi("TestSessionToken"))

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertEquals(result.data, "TestSessionToken")
    }

    @Test
    fun `Login - If username and password are empty, should return an empty credentials error`() {
        val result = runBlocking {
            auth.login("", "")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmptyCredentialsError::class.java))
    }

    @Test
    fun `Login - If error code 101 as json in response, should return an invalid credentials error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Invalid username/password.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.InvalidCredentials::class.java))
    }

    @Test
    fun `Login - If error code 209 as json in response, should return an invalid session token error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":209,\"error\":\"Invalid session token.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.InvalidSessionToken::class.java))
    }

    @Test
    fun `Login - If error code 124 as json in response, should return a time out error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout connection.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.TimeOutError::class.java))
    }

    @Test
    fun `Login - If error code 125 as json in response, should return a time out error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":125,\"error\":\"Invalid email error.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.InvalidEmailError::class.java))
    }

    @Test
    fun `Login - If error code 202 as json in response, should return an username already taken error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":202,\"error\":\"Username already taken.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.UsernameAlreadyTaken::class.java))
    }

    @Test
    fun `Login - If error code 125 as json in response, should return an email already taken error`() {
        coEvery { authApi.getSessionToken(any(), any()) } returns Response.error(404, "{\"code\":203,\"error\":\"Email already taken.\"}".toResponseBody())

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmailAlreadyTaken::class.java))
    }

    @Test
    fun `Login - If http error code 401 throws, should return an unauthorized error`() {
        coEvery { authApi.getSessionToken(any(), any()) } throws HttpException(Response.error<Any>(401, "{\"code\":101,\"error\":\"Invalid username/password.\"}".toResponseBody()))

        val result = runBlocking {
            auth.login("test", "test")
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
    }
    //endregion

    //region Authenticate
    @Test
    fun `Authenticate - Not null`() {
        coEvery { authApi.authenticate(any()) } returns Response.success(SessionTokenApi("TestSessionToken"))

        val result = runBlocking {
            auth.authenticate("test")
        }

        assertThat(result, instanceOf(AuthenticationResult.Authorized::class.java))
    }

    @Test
    fun `Authenticate - If session token is empty, return unauthorized`() {
        coEvery { authApi.authenticate(any()) } returns Response.success(SessionTokenApi("TestSessionToken"))

        val result = runBlocking {
            auth.authenticate("")
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
    }

    @Test
    fun `Authenticate - If session token is null, return unauthorized`() {
        coEvery { authApi.authenticate(any()) } returns Response.success(SessionTokenApi("TestSessionToken"))

        val result = runBlocking {
            auth.authenticate(null)
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
    }

    @Test
    fun `Authenticate - If request response that session is invalid, return invalid session`() {
        coEvery { authApi.authenticate(any()) } returns Response.error(404, "{\"code\":209,\"error\":\"Invalid session token.\"}".toResponseBody())

        val result = runBlocking {
            auth.authenticate("5d5d87d5")
        }

        assertThat(result.error, instanceOf(AuthenticationError.InvalidSessionToken::class.java))
    }
    //endregion

    //region Registration

    @Test
    fun `Registration - Not null`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.success(null)

        val result = runBlocking {
            auth.register("test", "test@", "test", "test", "test")
        }

        assertThat(result, instanceOf(AuthenticationResult.Registered::class.java))
    }

    @Test
    fun `Registration - If user data are empty, return empty credentials`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.success(null)

        val result = runBlocking {
            auth.register("", "", "", "", "")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmptyCredentialsError::class.java))
    }

    @Test
    fun `Registration - If some of the user data are empty, return empty credentials`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.success(null)

        val result = runBlocking {
            auth.register("test", "test@", "", "", "")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmptyCredentialsError::class.java))
    }

    @Test
    fun `Registration - If request response that email is already taken, return email already taken error`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.error(404, "{\"code\":203,\"error\":\"Email already taken.\"}".toResponseBody())

        val result = runBlocking {
            auth.register("test", "test@", "test", "test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.EmailAlreadyTaken::class.java))
    }

    @Test
    fun `Registration - If request response that username is already taken, return username already taken error`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.error(404, "{\"code\":202,\"error\":\"Username already taken.\"}".toResponseBody())

        val result = runBlocking {
            auth.register("test", "test@", "test", "test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.UsernameAlreadyTaken::class.java))
    }

    @Test
    fun `Registration - If email has the wrong format, return wrong email format error`() {
        coEvery { authApi.signUp(any(), any(), any(), any(), any()) } returns Response.success(null)

        val result = runBlocking {
            auth.register("test", "test", "test", "test", "test")
        }

        assertThat(result.error, instanceOf(AuthenticationError.WrongEmailFormatError::class.java))
    }
    //endregion

    //region Logout
    @Test
    fun `Logout - Not null`() {
        coEvery { authApi.logout(any()) } returns Response.success(null)

        val result = runBlocking {
            auth.logout("test")
        }

        assertThat(result, instanceOf(AuthenticationResult.LoggedOut::class.java))
    }

    @Test
    fun `Logout - If session token empty, should return unauthorized`() {
        coEvery { authApi.logout(any()) } returns Response.error(404, "".toResponseBody())

        val result = runBlocking {
            auth.logout("")
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
    }

    @Test
    fun `Logout - If session token null, should return unauthorized`() {
        coEvery { authApi.logout(any()) } returns Response.error(404, "".toResponseBody())

        val result = runBlocking {
            auth.logout(null)
        }

        assertThat(result, instanceOf(AuthenticationResult.Unauthorized::class.java))
    }

    @Test
    fun `Logout - If there is any error in the response, should return logout anyway`() {
        coEvery { authApi.logout(any()) } returns Response.error(404, "".toResponseBody())

        val result = runBlocking {
            auth.logout("90klsdoik")
        }

        assertThat(result, instanceOf(AuthenticationResult.LoggedOut::class.java))
    }
    //endregion
}
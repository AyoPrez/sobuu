package com.ayoprez.sobuu.shared.features.authentication.remote

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class AuthenticationRemoteDataImpl @Inject constructor(
    private val authApi: AuthenticationApi
): IAuthenticationRemoteData {

    override suspend fun login(username: String, password: String): AuthenticationResult<String> {
        return try {
            if(username.isBlank() || password.isBlank()) {
                return AuthenticationResult.Error(AuthenticationError.EmptyCredentialsError)
            }

            val response = authApi.getSessionToken(username, password)

            if(response.body() == null && response.errorBody() != null) {
                errorBodyHandle(response.errorBody()!!)
            } else {
                AuthenticationResult.Authorized(response.body()?.sessionToken)
            }
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthenticationResult.Unauthorized()
            } else {
                AuthenticationResult.Error(AuthenticationError.UnknownError)
            }
        } catch (e: Exception) {
            AuthenticationResult.Error(AuthenticationError.UnknownError)
        }
    }

    override suspend fun logout(sessionToken: String?): AuthenticationResult<Unit> {
        return try {
            if(sessionToken.isNullOrBlank()) {
                return AuthenticationResult.Unauthorized()
            }

            authApi.logout(sessionToken)

            AuthenticationResult.LoggedOut()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthenticationResult.Unauthorized()
            } else {
                AuthenticationResult.Error(AuthenticationError.UnknownError)
            }
        } catch (e: Exception) {
            AuthenticationResult.Error(AuthenticationError.UnknownError)
        }
    }

    override suspend fun register(username: String, email: String, password: String, firstname: String, lastname: String): AuthenticationResult<Unit> {
        return try {
            if(username.isBlank() || password.isBlank() || email.isBlank() || firstname.isBlank() || lastname.isBlank()) {
                return AuthenticationResult.Error(AuthenticationError.EmptyCredentialsError)
            }

            if(!email.contains('@')) {
                return AuthenticationResult.Error(AuthenticationError.WrongEmailFormatError)
            }

            val response = authApi.signUp(username, email, password, firstname, lastname)

            if(response.body() == null && response.errorBody() != null) {
                errorBodyHandle(response.errorBody()!!)
            } else {
                AuthenticationResult.Registered()
            }
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthenticationResult.Unauthorized()
            } else {
                AuthenticationResult.Error(AuthenticationError.UnknownError)
            }
        }
    }

    override suspend fun authenticate(sessionToken: String?): AuthenticationResult<Unit> {
        return try {
            if (sessionToken.isNullOrBlank()) {
                return AuthenticationResult.Unauthorized()
            }

            val response = authApi.authenticate(sessionToken)

            if(response.body() == null && response.errorBody() != null) {
                errorBodyHandle(response.errorBody()!!)
            } else {
                AuthenticationResult.Authorized()
            }
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> {
                    AuthenticationResult.Unauthorized()
                }
                209 -> {
                    AuthenticationResult.Error(AuthenticationError.InvalidSessionToken)
                }
                else -> {
                    AuthenticationResult.Error(AuthenticationError.UnknownError)
                }
            }
        } catch (e: Exception) {
            AuthenticationResult.Error(AuthenticationError.UnknownError)
        }
    }

    override suspend fun resetPassword(email: String?): AuthenticationResult<Unit> {
        return try {
            if (email.isNullOrBlank()) {
                return AuthenticationResult.Error(AuthenticationError.EmptyCredentialsError)
            }

            val response = authApi.resetPassword(email)

            if(response.body() == null && response.errorBody() != null) {
                errorBodyHandle(response.errorBody()!!)
            } else {
                AuthenticationResult.ResetPassword()
            }
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> {
                    AuthenticationResult.Unauthorized()
                }
                209 -> {
                    AuthenticationResult.Error(AuthenticationError.InvalidSessionToken)
                }
                else -> {
                    AuthenticationResult.Error(AuthenticationError.UnknownError)
                }
            }
        } catch (e: Exception) {
            AuthenticationResult.Error(AuthenticationError.UnknownError)
        }
    }

    private fun <T>errorBodyHandle(errorBody: ResponseBody): AuthenticationResult<T> {
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when(it) {
                    101 -> AuthenticationResult.Error(AuthenticationError.InvalidCredentials)
                    209 -> AuthenticationResult.Error(AuthenticationError.InvalidSessionToken)
                    124 -> AuthenticationResult.Error(AuthenticationError.TimeOutError)
                    125 -> AuthenticationResult.Error(AuthenticationError.InvalidEmailError)
                    202 -> AuthenticationResult.Error(AuthenticationError.UsernameAlreadyTaken)
                    203 -> AuthenticationResult.Error(AuthenticationError.EmailAlreadyTaken)
                    else -> AuthenticationResult.Error(AuthenticationError.UnknownError)
                }
            }
    }
}
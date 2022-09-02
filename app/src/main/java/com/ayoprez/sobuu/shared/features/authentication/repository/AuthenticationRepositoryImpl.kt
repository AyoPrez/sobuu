package com.ayoprez.sobuu.shared.features.authentication.repository

import com.ayoprez.sobuu.shared.features.authentication.database.IAuthenticationLocalData
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.shared.features.authentication.remote.IAuthenticationRemoteData
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authRemoteData: IAuthenticationRemoteData,
    private val authLocalData: IAuthenticationLocalData
) : IAuthenticationRepository {

    override suspend fun loginUser(username: String, password: String): AuthenticationResult<Unit> {
        return when(val result = authRemoteData.login(username, password)) {
            is AuthenticationResult.Authorized -> {
                val token = authRemoteData.login(username, password).data
                setSessionToken(token)
                AuthenticationResult.Authorized()
            }
            is AuthenticationResult.Unauthorized -> {
                AuthenticationResult.Unauthorized()
            }
            is AuthenticationResult.LoggedOut -> {
                setSessionToken(null)
                AuthenticationResult.LoggedOut()
            }
            is AuthenticationResult.Error -> {
                AuthenticationResult.Error(result.error)
            }
            is AuthenticationResult.Registered -> {
                AuthenticationResult.Registered()
            }
            is AuthenticationResult.ResetPassword -> {
                AuthenticationResult.ResetPassword()
            }
        }
    }

    override suspend fun logoutUser(): AuthenticationResult<Unit> {
        val token = authLocalData.getSessionToken() ?: return AuthenticationResult.Unauthorized()
        val result = authRemoteData.logout(token)

        authLocalData.setSessionToken(null)
        return result
    }

    override suspend fun registerUser(username: String, email: String,
                                      password: String, firstname: String,
                                      lastname: String): AuthenticationResult<Unit> {
        return authRemoteData.register(
            username = username, email = email,
            password = password, firstname = firstname,
            lastname = lastname)
    }

    override suspend fun authenticate(): AuthenticationResult<Unit> {
        val token = authLocalData.getSessionToken() ?: return AuthenticationResult.Unauthorized()

        val result = authRemoteData.authenticate(token)

        return if(result is AuthenticationResult.Error) {
            setSessionToken(null)
            AuthenticationResult.Unauthorized()
        } else {
            result
        }
    }

    override suspend fun resetPassword(email: String?): AuthenticationResult<Unit> {
        return authRemoteData.resetPassword(email)
    }

    override suspend fun getSessionToken(username: String, password: String): String? {
        return authLocalData.getSessionToken()
    }

    override suspend fun setSessionToken(token: String?) {
        authLocalData.setSessionToken(token)
    }
}
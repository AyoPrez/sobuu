package com.ayoprez.sobuu.shared.features.authentication.repository

import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult

interface IAuthenticationRepository {

    suspend fun loginUser(username: String, password: String): AuthenticationResult<Unit>

    suspend fun logoutUser(): AuthenticationResult<Unit>

    suspend fun registerUser(username: String, email: String, password: String, firstname: String, lastname: String): AuthenticationResult<Unit>

    suspend fun authenticate(): AuthenticationResult<Unit>

    suspend fun getSessionToken(username: String, password: String): String?

    suspend fun setSessionToken(token: String?)
}
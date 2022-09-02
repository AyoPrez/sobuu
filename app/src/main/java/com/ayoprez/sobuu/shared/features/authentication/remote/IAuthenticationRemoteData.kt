package com.ayoprez.sobuu.shared.features.authentication.remote

interface IAuthenticationRemoteData {

    suspend fun login(username: String, password: String): AuthenticationResult<String>

    suspend fun logout(sessionToken: String?): AuthenticationResult<Unit>

    suspend fun register(username: String, email: String, password: String, firstname: String, lastname: String): AuthenticationResult<Unit>

    suspend fun authenticate(sessionToken: String?): AuthenticationResult<Unit>

    suspend fun resetPassword(email: String?): AuthenticationResult<Unit>
}
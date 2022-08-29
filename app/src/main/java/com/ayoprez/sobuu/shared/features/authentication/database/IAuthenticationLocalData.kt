package com.ayoprez.sobuu.shared.features.authentication.database

interface IAuthenticationLocalData {

    suspend fun getSessionToken(): String?

    suspend fun setSessionToken(token: String?)
}
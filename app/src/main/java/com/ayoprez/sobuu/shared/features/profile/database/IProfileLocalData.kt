package com.ayoprez.sobuu.shared.features.profile.database

interface IProfileLocalData {
    suspend fun getSessionToken(): String?
}
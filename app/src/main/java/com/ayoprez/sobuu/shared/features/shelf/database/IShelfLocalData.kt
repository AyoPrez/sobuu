package com.ayoprez.sobuu.shared.features.shelf.database

interface IShelfLocalData {

    suspend fun getSessionToken(): String?

}
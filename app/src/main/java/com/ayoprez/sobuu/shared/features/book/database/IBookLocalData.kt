package com.ayoprez.sobuu.shared.features.book.database

interface IBookLocalData {
    suspend fun getSessionToken(): String?
}
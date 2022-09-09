package com.ayoprez.sobuu.shared.features.comments.database

interface ICommentLocalData {
    suspend fun getSessionToken(): String?
}
package com.ayoprez.sobuu.shared.models.api_models.books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val firstName: String,
    val id: String,
    val lastName: String,
    val username: String
)
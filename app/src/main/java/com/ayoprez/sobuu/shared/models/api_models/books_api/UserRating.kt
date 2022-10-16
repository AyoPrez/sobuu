package com.ayoprez.sobuu.shared.models.api_models.books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRating(
    val date: Date,
    val id: String,
    val rating: Double,
    val review: String,
    val user: User
)
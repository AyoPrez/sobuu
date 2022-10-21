package com.ayoprez.sobuu.shared.models.api_models.finished_books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserBookRating(
    val date: Date,
    val id: String,
    val rate: Double,
    val review: String?
)
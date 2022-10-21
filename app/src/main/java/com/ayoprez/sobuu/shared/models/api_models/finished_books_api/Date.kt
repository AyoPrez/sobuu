package com.ayoprez.sobuu.shared.models.api_models.finished_books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Date(
    val __type: String,
    val iso: String
)
package com.ayoprez.sobuu.shared.models.api_models.finished_books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartedToRead(
    val __type: String,
    val iso: String
)
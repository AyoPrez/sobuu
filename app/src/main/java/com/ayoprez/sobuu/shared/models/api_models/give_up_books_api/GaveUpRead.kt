package com.ayoprez.sobuu.shared.models.api_models.give_up_books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GaveUpRead(
    val __type: String,
    val iso: String
)
package com.ayoprez.sobuu.shared.models.api_models.books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksApi(
    val result: List<Result>
)
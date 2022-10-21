package com.ayoprez.sobuu.shared.models.api_models.books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val authors: List<String>,
    val credits: List<String>?,
    val description: String,
    val extras: Extras?,
    val genres: List<String>,
    val id: String?,
    val isbn: List<String>,
    val picture: String,
    val thumbnail: String,
    val publishedDate: String,
    val publisher: String,
    val title: String,
    val totalPages: Int
)
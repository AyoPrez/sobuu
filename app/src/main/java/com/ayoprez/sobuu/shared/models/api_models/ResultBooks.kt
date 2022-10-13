package com.ayoprez.sobuu.shared.models.api_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultBooks(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "authors")
    val authors: List<String>,
    @Json(name = "description")
    val description: String,
    @Json(name = "publishedDate")
    val publishedDate: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "picture")
    val picture: String,
    @Json(name = "credits")
    val credits: List<String>?,
    @Json(name = "totalPages")
    val totalPages: Int,
    @Json(name = "genres")
    val genres: List<String>,
    @Json(name = "isbn")
    val isbn: List<String>,
    @Json(name = "extras")
    val extras: BookExtras?,
)
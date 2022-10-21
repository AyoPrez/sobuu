package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val authors: List<String>,
    val extras: Extras,
    val id: String,
    val picture: String,
    val title: String
)
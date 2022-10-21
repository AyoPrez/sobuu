package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentlyReadingBookApi(
    val result: List<Result>
)
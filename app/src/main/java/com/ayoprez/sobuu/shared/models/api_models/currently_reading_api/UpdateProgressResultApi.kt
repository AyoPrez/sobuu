package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProgressResultApi(
    val result: Result
)

package com.ayoprez.sobuu.shared.models.api_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Books(
    @Json(name = "result") val result: List<ResultBooks>
)

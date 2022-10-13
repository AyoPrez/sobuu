package com.ayoprez.sobuu.shared.models.api_models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetUserProfile(
    val result: Result
)
package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extras(
    val commentsInProgress: List<CommentsInProgress>,
    val currentPercentage: Int? = null,
    val currentProgress: Int,
    val currentPage: Int? = null,
    val startedToRead: StartedToRead
)
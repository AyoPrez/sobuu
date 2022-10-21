package com.ayoprez.sobuu.shared.models.api_models.give_up_books_api

import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.CommentsInProgress
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extras(
    val commentsInProgress: List<CommentsInProgress>,
    val currentPage: Int?,
    val currentPercentage: Int?,
    val currentProgress: Double,
    val gaveUpRead: GaveUpRead,
    val startedToRead: StartedToRead
)
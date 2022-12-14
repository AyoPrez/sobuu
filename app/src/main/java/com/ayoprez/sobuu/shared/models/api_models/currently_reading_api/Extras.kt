package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.ayoprez.sobuu.shared.models.api_models.finished_books_api.FinishedToRead
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extras(
    val commentsInProgress: List<CommentsInProgress>,
    val currentPercentage: Int? = null,
    val currentProgress: Double,
    val currentPage: Int? = null,
    val startedToRead: StartedToRead,
    val finishedToRead: FinishedToRead? = null,
    val finished: Boolean = false,
    val giveUp: Boolean = false,
)
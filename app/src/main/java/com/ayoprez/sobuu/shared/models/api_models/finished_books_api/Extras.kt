package com.ayoprez.sobuu.shared.models.api_models.finished_books_api

import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.CommentsInProgress
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extras(
    val commentsInProgress: List<CommentsInProgress>,
    val currentPercentage: Int,
    val currentProgress: Int,
    val finishedToRead: FinishedToRead,
    val startedToRead: StartedToRead,
    val userBookRating: UserBookRating
)
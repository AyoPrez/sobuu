package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsInProgress(
    val date: Date,
    val hasSpoilers: Boolean,
    val id: String,
    val text: String,
    val username: String,
    val votes: Int
)
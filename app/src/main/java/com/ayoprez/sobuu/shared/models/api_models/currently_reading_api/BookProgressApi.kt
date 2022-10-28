package com.ayoprez.sobuu.shared.models.api_models.currently_reading_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookProgressApi(
    val id: String? = null,
    val percentage: Double?,
    val page: Int?,
    val progressInPercentage: Double,
    val finished: Boolean = false,
    val giveUp: Boolean = false,
    val startedToRead: Date,
    val finishedToRead: Date? = null,
)
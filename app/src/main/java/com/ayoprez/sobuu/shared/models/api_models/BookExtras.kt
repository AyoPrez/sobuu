package com.ayoprez.sobuu.shared.models.api_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookExtras (
    @Json(name = "totalComments")
    val totalComments: Int = 0,
    @Json(name = "peopleReadingIt")
    val peopleReadingIt: Int = 0,
    @Json(name = "readingStatus")
    val readingStatus: Int,
//    @Json(name = "allReviews")
//    val allReviews: List<UserBookRating> = emptyList(),
//    @Json(name = "userRating")
//    val userRating: UserBookRating?,
    @Json(name = "totalRating")
    val totalRating: Double? = 0.0,
)


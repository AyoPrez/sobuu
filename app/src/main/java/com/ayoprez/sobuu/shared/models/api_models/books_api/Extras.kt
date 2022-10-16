package com.ayoprez.sobuu.shared.models.api_models.books_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extras(
    val allReviews: List<AllReview>,
    val peopleReadingIt: Int,
    val readingStatus: Int,
    val totalComments: Int,
    val totalRating: Double,
    val userRating: UserRating?
)
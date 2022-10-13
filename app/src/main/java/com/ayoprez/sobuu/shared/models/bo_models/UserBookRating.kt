package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class UserBookRating(
    @Json(name = "id")
    val id: String,
    @Json(name = "book")
    val book: Book?,
    @Json(name = "user")
    val user: Profile,
    @Json(name = "rating")
    val rating: Double,
    @Json(name = "review")
    val review: String,
    @Json(name = "date")
    val date: LocalDateTime) : Parcelable

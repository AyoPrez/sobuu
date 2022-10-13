package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

enum class BookReadingStatus(val value: Int) {
    NOT_READ(0),
    READING(1),
    FINISHED(2),
    GIVE_UP(3)
}

@JsonClass(generateAdapter = true)
@Parcelize
data class Book(val id: String,
                val title: String,
                val authors: List<String>,
                val description: String,
                val picture: String,
                val publisher: String,
                val credits: List<String>?,
                val totalPages: Int,
                val isbn: List<String>,
                val publishedDate: String,
                val genres: List<String>,
                val totalComments: Int,
                val peopleReadingIt: Int,
                val readingStatus: BookReadingStatus,
                val allReviews: List<UserBookRating>,
                val userRating: UserBookRating?,
                val totalRating: Double,
) : Parcelable
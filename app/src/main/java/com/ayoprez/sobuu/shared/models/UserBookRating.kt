package com.ayoprez.sobuu.shared.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserBookRating(val id: String,
                          val book: Book,
                          val user: Profile,
                          val rating: Double,
                          val review: String) : Parcelable

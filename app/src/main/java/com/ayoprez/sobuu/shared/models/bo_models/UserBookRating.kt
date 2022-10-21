package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class UserBookRating(
    val id: String,
    val book: Book? = null,
    val user: Profile? = null,
    val rating: Double,
    val review: String,
    val date: LocalDateTime
    ) : Parcelable

package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentlyReadingBook(
    val id: String,
    val title: String,
    val authors: List<String>,
    val picture: String,
    val bookProgress: BookProgress? = null,
    val bookProgressComments: List<Comment>? = null,
): Parcelable

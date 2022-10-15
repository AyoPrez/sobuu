package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Profile(val id: String,
                   val giveUp: List<Book> = emptyList(),
                   val alreadyRead: List<Book> = emptyList(),
                   val firstName: String,
                   val lastName: String,
                   val following: List<Profile> = emptyList(),
                   val userShelves: List<Shelf> = emptyList(),
                   val bookProgress: List<BookProgress> = emptyList()
                   ) : Parcelable
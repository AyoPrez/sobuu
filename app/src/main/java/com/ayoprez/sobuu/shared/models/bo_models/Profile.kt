package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Profile(val id: String,
                   val giveUp: List<Book>,
                   val alreadyRead: List<Book>,
                   val firstName: String,
                   val lastName: String,
                   val following: List<Profile>,
                   val userShelves: List<Shelf>,
                   val bookProgress: List<BookProgress>
                   ) : Parcelable
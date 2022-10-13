package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Shelf(val id: String,
                 val books: List<Book>,
                 val name: String,
                 val description: String,
                 val isPublic: Boolean) : Parcelable

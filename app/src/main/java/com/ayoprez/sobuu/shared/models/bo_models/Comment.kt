package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class Comment(val id: String,
                   val user: Profile,
                   val publishedDate: LocalDateTime,
                   val text: String,
                   val hasSpoilers: Boolean,
                   val votesCounter: Long,
                   val percentage: Byte?,
                   val pageNumber: Int?,
                   val parentCommentId: String?) : Parcelable

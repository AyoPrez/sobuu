package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class BookProgress(
    val id: String? = null,
    val percentage: Number?,
    val page: Number?,
    val progressInPercentage: Number,
    val finished: Boolean = false,
    val giveUp: Boolean = false,
    val startedToRead: LocalDateTime,
    val finishedToRead: LocalDateTime? = null,
) : Parcelable
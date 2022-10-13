package com.ayoprez.sobuu.shared.models.bo_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookProgress(val id: String,
                val percentage: Number?,
                val page: Number?,
                val finished: Boolean,
                val giveUp: Boolean,
) : Parcelable
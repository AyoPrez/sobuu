package com.ayoprez.sobuu.presentation.profile

import android.os.Parcelable
import com.ayoprez.sobuu.shared.models.Profile
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileState(
    val profileInfo: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
    ) : Parcelable

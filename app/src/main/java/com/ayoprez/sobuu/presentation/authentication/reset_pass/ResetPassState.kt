package com.ayoprez.sobuu.presentation.authentication.reset_pass

import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError

data class ResetPassState(
    val isLoading: Boolean = false,
    val forgotEmail: String = "",
    val error: AuthenticationError? = null,
)

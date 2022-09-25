package com.ayoprez.sobuu.presentation.authentication.reset_pass

data class ResetPassState(
    val isLoading: Boolean = false,
    val forgotEmail: String = "",
)

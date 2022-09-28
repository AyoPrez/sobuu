package com.ayoprez.sobuu.presentation.authentication.login

import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError

data class LoginState(
    val isLoading: Boolean = false,
    val loginUsername: String = "",
    val loginPassword: String = "",
    val error: AuthenticationError? = null,
)

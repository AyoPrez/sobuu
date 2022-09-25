package com.ayoprez.sobuu.presentation.authentication.login

data class LoginState(
    val isLoading: Boolean = false,
    val loginUsername: String = "",
    val loginPassword: String = "",
)

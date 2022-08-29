package com.ayoprez.sobuu.presentation.authentication

data class AuthenticationState(
    val isLoading: Boolean = false,
    val registrationUsername: String = "",
    val registrationEmail: String = "",
    val registrationPassword: String = "",
    val registrationFirstname: String = "",
    val registrationLastname: String = "",
    val loginUsername: String = "",
    val loginPassword: String = "",
)

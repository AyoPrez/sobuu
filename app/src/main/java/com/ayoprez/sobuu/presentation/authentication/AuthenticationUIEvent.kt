package com.ayoprez.sobuu.presentation.authentication

sealed class AuthenticationUIEvent {
    data class RegistrationUsernameChanged(val value: String): AuthenticationUIEvent()
    data class RegistrationPasswordChanged(val value: String): AuthenticationUIEvent()
    object registerUser: AuthenticationUIEvent()

    object logoutUser: AuthenticationUIEvent()
}

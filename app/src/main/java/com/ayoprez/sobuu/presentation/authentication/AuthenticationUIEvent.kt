package com.ayoprez.sobuu.presentation.authentication

sealed class AuthenticationUIEvent {
    data class RegistrationUsernameChanged(val value: String): AuthenticationUIEvent()
    data class RegistrationPasswordChanged(val value: String): AuthenticationUIEvent()
    object registerUser: AuthenticationUIEvent()

    data class LoginUsernameChanged(val value: String): AuthenticationUIEvent()
    data class LoginPasswordChanged(val value: String): AuthenticationUIEvent()
    object loginUser: AuthenticationUIEvent()
    object logoutUser: AuthenticationUIEvent()

}

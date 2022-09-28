package com.ayoprez.sobuu.presentation.authentication.login

sealed class LoginUIEvent {
    data class LoginUsernameChanged(val value: String): LoginUIEvent()
    data class LoginPasswordChanged(val value: String): LoginUIEvent()
    object loginUser: LoginUIEvent()
    object removeErrorState: LoginUIEvent()
}

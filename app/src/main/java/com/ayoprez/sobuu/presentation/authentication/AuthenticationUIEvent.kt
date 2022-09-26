package com.ayoprez.sobuu.presentation.authentication

sealed class AuthenticationUIEvent {
    object logoutUser: AuthenticationUIEvent()
}

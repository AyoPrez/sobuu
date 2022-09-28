package com.ayoprez.sobuu.presentation.authentication.splash

import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError

data class SplashState (
    val isLoading: Boolean = false,
    val error: AuthenticationError? = null
)
package com.ayoprez.sobuu.presentation.authentication.reset_pass

sealed class ResetPassUIEvent {
    data class ForgotPasswordEmailChanged(val value: String): ResetPassUIEvent()
    object resetPassword: ResetPassUIEvent()
}

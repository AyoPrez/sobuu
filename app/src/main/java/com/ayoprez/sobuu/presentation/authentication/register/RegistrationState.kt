package com.ayoprez.sobuu.presentation.authentication.register

data class RegistrationState (
    val isLoading: Boolean = false,
    val registrationUsername: String = "",
    val registrationEmail: String = "",
    val registrationPassword: String = "",
    val registrationPasswordConfirmation: String = "",
    val registrationFirstname: String = "",
    val registrationLastname: String = "",
    val privacyPolicyConfirmationSwitch: Boolean = false,
)

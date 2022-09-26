package com.ayoprez.sobuu.presentation.authentication.register

sealed class RegistrationUIEvent {
    data class RegistrationUsernameChanged(val value: String): RegistrationUIEvent()
    data class RegistrationPasswordChanged(val value: String): RegistrationUIEvent()
    data class RegistrationPasswordConfirmationChanged(val value: String): RegistrationUIEvent()
    data class RegistrationEmailChanged(val value: String): RegistrationUIEvent()
    data class RegistrationFirstNameChanged(val value: String): RegistrationUIEvent()
    data class RegistrationLastNameChanged(val value: String): RegistrationUIEvent()
    data class PrivacyPolicyConfirmationSwitchChanged(val value: Boolean): RegistrationUIEvent()
    object registerUser: RegistrationUIEvent()
}

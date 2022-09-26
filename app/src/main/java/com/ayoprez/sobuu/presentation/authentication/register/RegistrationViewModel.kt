package com.ayoprez.sobuu.presentation.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.shared.features.authentication.repository.AuthenticationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val auth: AuthenticationRepositoryImpl): ViewModel() {

    var state by mutableStateOf(RegistrationState())

    private val resultChannel = Channel<AuthenticationResult<Unit>>()
    val registrationResult = resultChannel.receiveAsFlow()


    fun onEvent(event: RegistrationUIEvent) {
        when(event) {
            is RegistrationUIEvent.RegistrationPasswordChanged -> {
                state = state.copy(registrationPassword = event.value)
            }
            is RegistrationUIEvent.RegistrationUsernameChanged -> {
                state = state.copy(registrationUsername = event.value)
            }
            is RegistrationUIEvent.registerUser -> registration()
            is RegistrationUIEvent.RegistrationEmailChanged -> {
                state = state.copy(registrationEmail = event.value)
            }
            is RegistrationUIEvent.RegistrationFirstNameChanged -> {
                state = state.copy(registrationFirstname = event.value)
            }
            is RegistrationUIEvent.RegistrationLastNameChanged -> {
                state = state.copy(registrationLastname = event.value)
            }
            is RegistrationUIEvent.RegistrationPasswordConfirmationChanged -> {
                state = state.copy(registrationPasswordConfirmation = event.value)
            }
            is RegistrationUIEvent.PrivacyPolicyConfirmationSwitchChanged -> {
                state = state.copy(privacyPolicyConfirmationSwitch = event.value)
            }
        }
    }

    fun handleError(error: AuthenticationError?) {
        //TODO Add the rest of the errors
        //TODO Check what to do here with the errors. I don't have the context, so I should to get the strings here
        when(error){
            is AuthenticationError.InvalidCredentials -> {
            }
            is AuthenticationError.EmptyCredentialsError -> {

            }
            else -> {

            }
        }
    }

    private fun registration() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.registerUser(
                username = state.registrationUsername,
                email = state.registrationEmail,
                password = state.registrationPassword,
                firstname = state.registrationFirstname,
                lastname = state.registrationLastname,
            )

            resultChannel.send(result)

            state = state.copy(isLoading = false)
        }
    }
}
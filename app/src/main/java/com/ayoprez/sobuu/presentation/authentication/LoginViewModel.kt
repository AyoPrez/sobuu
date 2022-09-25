package com.ayoprez.sobuu.presentation.authentication

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
class LoginViewModel @Inject constructor(private val auth: AuthenticationRepositoryImpl): ViewModel() {

    var state by mutableStateOf(AuthenticationState())

    private val resultChannel = Channel<AuthenticationResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authentication()
    }

    fun onEvent(event: AuthenticationUIEvent) {
        when(event) {
            is AuthenticationUIEvent.LoginPasswordChanged -> {
                state = state.copy(loginPassword = event.value)
            }
            is AuthenticationUIEvent.LoginUsernameChanged -> {
                state = state.copy(loginUsername = event.value)
            }
            is AuthenticationUIEvent.RegistrationPasswordChanged -> {
                state = state.copy(registrationPassword = event.value)
            }
            is AuthenticationUIEvent.RegistrationUsernameChanged -> {
                state = state.copy(registrationUsername = event.value)
            }
            AuthenticationUIEvent.loginUser -> login()
            AuthenticationUIEvent.registerUser -> registration()
            AuthenticationUIEvent.logoutUser -> logout()
            AuthenticationUIEvent.createNewAccount -> openCreateNewAccountScreen()
            AuthenticationUIEvent.forgotPassword -> openForgotPasswordScreen()
            AuthenticationUIEvent.openPrivacyPolicy -> openPrivacyPolicy()
            AuthenticationUIEvent.openTermsAndConditions -> openTermsAndConditions()
            AuthenticationUIEvent.resetPassword -> resetPassword()
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

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.loginUser(
                username = state.loginUsername,
                password = state.loginPassword
            )

            resultChannel.send(result)

            state = state.copy(isLoading = false)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.logoutUser()

            resultChannel.send(result)

            state = state.copy(isLoading = false)
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

    private fun authentication() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.authenticate()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun openCreateNewAccountScreen() {

    }

    private fun openForgotPasswordScreen() {

    }

    private fun openPrivacyPolicy() {

    }

    private fun openTermsAndConditions() {}

    private fun resetPassword() {

    }
}
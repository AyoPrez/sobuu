package com.ayoprez.sobuu.presentation.authentication.login

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

    var state by mutableStateOf(LoginState())

    private val resultChannel = Channel<AuthenticationResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginUIEvent) {
        when(event) {
            is LoginUIEvent.LoginPasswordChanged -> {
                state = state.copy(loginPassword = event.value)
            }
            is LoginUIEvent.LoginUsernameChanged -> {
                state = state.copy(loginUsername = event.value)
            }
            is LoginUIEvent.loginUser -> login()
            is LoginUIEvent.removeErrorState -> removeErrorState()
        }
    }

    fun handleError(error: AuthenticationError?) {
        state = state.copy(error = error)
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

    private fun removeErrorState() {
        viewModelScope.launch {
            state = state.copy(error = null)
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
}
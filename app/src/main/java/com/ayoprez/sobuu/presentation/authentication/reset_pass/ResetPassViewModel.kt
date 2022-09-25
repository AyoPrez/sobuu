package com.ayoprez.sobuu.presentation.authentication.reset_pass

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
class ResetPassViewModel @Inject constructor(private val auth: AuthenticationRepositoryImpl): ViewModel() {

    var state by mutableStateOf(ResetPassState())

    private val resultChannel = Channel<AuthenticationResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()


    fun onEvent(event: ResetPassUIEvent) {
        when(event) {
            is ResetPassUIEvent.ForgotPasswordEmailChanged -> {
                state = state.copy(forgotEmail = event.value)
            }
            ResetPassUIEvent.resetPassword -> resetPassword()
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

    private fun resetPassword() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.resetPassword(
                email = state.forgotEmail
            )

            resultChannel.send(result)

            state = state.copy(isLoading = false)
        }
    }
}
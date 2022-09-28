package com.ayoprez.sobuu.presentation.authentication.splash

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
class SplashViewModel @Inject constructor(private val auth: AuthenticationRepositoryImpl): ViewModel() {

    var state by mutableStateOf(SplashState())

    private val resultChannel = Channel<AuthenticationResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authentication()
    }

    fun handleError(error: AuthenticationError?) {
        state = state.copy(error = error)
    }

    private fun authentication() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = auth.authenticate()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}
package com.ayoprez.sobuu.presentation.profile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileError
import com.ayoprez.sobuu.shared.features.profile.remote.ProfileResult
import com.ayoprez.sobuu.shared.features.profile.repository.IProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepo: IProfileRepository,
    private val app: Application
): ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

//    private val resultChannel = Channel<ProfileResult<Profile>>()
//    val profileResult = resultChannel.receiveAsFlow()

    init {
        loadUserProfile()
    }

    fun onEvent(event: ProfileUIEvent) {
        when(event) {
            ProfileUIEvent.startScreen -> loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null,
            )

            state = when(val result = profileRepo.getUserProfile()) {
                is ProfileResult.Error -> {
//                    resultChannel.send(result)
                    state.copy(
                        isLoading = false,
                        error = handleProfileErrors(result.error)
                    )
                }
                is ProfileResult.Success -> {
//                    resultChannel.send(result)
                    state.copy(
                        profileInfo = result.data,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun handleProfileErrors(error: ProfileError?): String {
        return when(error) {
            ProfileError.InvalidProfileIdError -> TODO()
            ProfileError.InvalidSessionTokenError -> TODO()
            ProfileError.ProcessingQueryError -> TODO()
            ProfileError.TimeOutError -> TODO()
            ProfileError.UnauthorizedQueryError -> TODO()
            ProfileError.UnknownError -> app.getString(R.string.unknown_error)
            null -> TODO()
        }
    }

}
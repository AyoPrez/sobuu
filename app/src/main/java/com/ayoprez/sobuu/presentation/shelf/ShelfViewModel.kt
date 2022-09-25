package com.ayoprez.sobuu.presentation.shelf

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfError
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfResult
import com.ayoprez.sobuu.shared.features.shelf.repository.IShelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShelfViewModel @Inject constructor(
    private val shelfRepository: IShelfRepository,
    private val app: Application
): ViewModel() {

    var state by mutableStateOf(ShelfState())
        private set

    fun loadUserShelves() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null,
            )

            state = when(val result = shelfRepository.getAllUserShelves()) {
                is ShelfResult.Error -> {
                    state.copy(
                        isLoading = false,
                        error = handleShelveErrors(result.error)
                    )
                }
                is ShelfResult.Success -> {
                    state.copy(
                        shelfList = result.data,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun handleShelveErrors(error: ShelfError?): String {
        return when(error) {
            ShelfError.EmptyBookId -> TODO()
            ShelfError.EmptyDescription -> app.getString(R.string.error_empty_description)
            ShelfError.EmptyName ->app.getString(R.string.error_empty_name)
            ShelfError.EmptyShelfId -> TODO()
            ShelfError.EmptyTerm -> app.getString(R.string.error_empty_term)
            ShelfError.InvalidSessionTokenError -> TODO()
            ShelfError.InvalidShelfId -> TODO()
            ShelfError.ProcessingQueryError -> TODO()
            ShelfError.TimeOutError -> TODO()
            ShelfError.UnauthorizedQueryError -> TODO()
            ShelfError.UnknownError -> TODO()
            null -> TODO()
        }
    }

}
package com.ayoprez.sobuu.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.models.bo_models.CurrentlyReadingBook
import com.ayoprez.sobuu.shared.models.bo_models.FinishedReadingBook
import com.ayoprez.sobuu.shared.models.bo_models.GiveUpBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val book: BookRepositoryImpl): ViewModel() {

    var currentSection: BookStatusType by mutableStateOf(BookStatusType.CURRENTLY_READING)
    var state by mutableStateOf(HomeState())
    var currentlyReadingBooksList by mutableStateOf<List<CurrentlyReadingBook>?>(emptyList())
    var finishedBooksList by mutableStateOf<List<FinishedReadingBook>?>(emptyList())
    var giveUpBooksList by mutableStateOf<List<GiveUpBook>?>(emptyList())

    init {
        onEvent(HomeUIEvent.DisplayCurrentlyReading)
    }

    fun onEvent(event: HomeUIEvent) {
        when(event) {
            HomeUIEvent.DisplayCurrentlyReading -> getUserCurrentReadingBooks()
            HomeUIEvent.DisplayFinished -> getUserFinishedBooks()
            HomeUIEvent.DisplayGiveUp -> getUserGiveUpBooks()
            is HomeUIEvent.DisplaySearch -> {
                state = state.copy(
                    isOnSearch = event.value,
                )
            }
        }
    }

    private fun handleError(error: BookError?) {
        state = state.copy(error = error)
    }

    private fun getUserCurrentReadingBooks() {
        currentSection = BookStatusType.CURRENTLY_READING
        state = state.copy(
            isOnSearch = false,
        )

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.getUserCurrentReadingBook()

            if(result.data.isNullOrEmpty()) {
                handleError(result.error)
            } else {
                currentlyReadingBooksList = result.data
                handleError(null)
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun getUserFinishedBooks() {
        currentSection = BookStatusType.ALREADY_READ
        state = state.copy(
            isOnSearch = false,

        )

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.getUserFinishedReadingBook()

            if(result.data.isNullOrEmpty()) {
                handleError(result.error)
            } else {
                finishedBooksList = result.data
                handleError(null)
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun getUserGiveUpBooks() {
        currentSection = BookStatusType.GIVE_UP
        state = state.copy(
            isOnSearch = false,
        )

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.getUserGiveUpBook()

            if(result.data.isNullOrEmpty()) {
                handleError(result.error)
            } else {
                giveUpBooksList = result.data
                handleError(null)
            }

            state = state.copy(isLoading = false)
        }
    }

}
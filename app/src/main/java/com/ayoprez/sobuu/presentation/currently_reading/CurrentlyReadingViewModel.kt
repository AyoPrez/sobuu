package com.ayoprez.sobuu.presentation.currently_reading

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.models.bo_models.BookProgress
import com.ayoprez.sobuu.shared.models.bo_models.CurrentlyReadingBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrentlyReadingViewModel @Inject constructor(private val bookRepo: BookRepositoryImpl): ViewModel() {

    var state by mutableStateOf(CurrentlyReadingState())
    private var pageProgress by mutableStateOf<Int?>(null)
    private var percentageProgress by mutableStateOf<Int?>(null)
    var bookData by mutableStateOf<CurrentlyReadingBook?>(null)
    var updatedProgress by mutableStateOf<BookProgress?>(null)

    fun onEvent(event: CurrentlyReadingUIEvent) {
        when(event) {
            is CurrentlyReadingUIEvent.UpdateProgress -> updateBookProgress(
                bookId = event.bookID,
                page = pageProgress,
                percentage = percentageProgress,
            )
            is CurrentlyReadingUIEvent.FinishBook -> finishBook(event.bookID)
            is CurrentlyReadingUIEvent.GiveUpBook -> giveUpBook(event.bookID)
            is CurrentlyReadingUIEvent.UpdateProgressChanged -> {
                pageProgress = event.page
                percentageProgress = event.percentage
            }
            is CurrentlyReadingUIEvent.FetchBookProgressData -> fetchBookProgress(event.bookId)
        }
    }

    fun handleError(error: BookError?) {
        state = state.copy(error = error)
    }

    private fun fetchBookProgress(bookId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = bookRepo.getBookProgress(bookId = bookId)

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                state = state.copy(progressUpdated = true)
                bookData = result.data
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun updateBookProgress(bookId: String, page: Int? = null, percentage: Int? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = bookRepo.updateBookProgress(
                bookId = bookId,
                percentage = percentage,
                page = page,
                finished = false,
                giveUp = false,
            )

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                state = state.copy(progressUpdated = true)
                fetchBookProgress(bookId = bookId)
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun finishBook(bookId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = bookRepo.finishBook(
                bookId = bookId
            )

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                state = state.copy(finished = true)
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun giveUpBook(bookId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = bookRepo.giveUpWithBook(
                bookId = bookId
            )

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                state = state.copy(gaveUp = true)
            }

            state = state.copy(isLoading = false)
        }
    }
}

package com.ayoprez.sobuu.presentation.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.features.shelf.repository.ShelfRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepo: BookRepositoryImpl,
    private val shelfRepo: ShelfRepositoryImpl,
) : ViewModel() {

    var state by mutableStateOf(BookState())

    fun onEvent(event: BookUIEvent) {
        when(event) {
            is BookUIEvent.AddBookToShelf -> addBookToShelf(event.bookId, event.shelfId)
            is BookUIEvent.DisplayUserShelves -> TODO()
            is BookUIEvent.StartToReadBook -> startToReadBook(event.bookId, event.bookTitle)
            is BookUIEvent.CancelAllErrors -> {
                state = state.copy(error = null)
            }
        }
    }

    fun handleError(error: BookError?) {
        state = state.copy(error = error)
    }

    private fun startToReadBook(bookId: String, bookTitle: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = bookRepo.setBookCurrentlyReading(bookId = bookId)

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                state = state.copy(
                    isLoading = false,
                    displayStartedToRead = true,
                    bookStartedReadingTitle = bookTitle,
                )
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun addBookToShelf(bookId: String, shelfId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = shelfRepo.addBookToShelf(shelfId = shelfId, bookId = bookId)

//            if(result.error != null) {
//                handleError(result.error)
//            } else {
//                handleError(null)
//            }

            state = state.copy(isLoading = false)
        }
    }
}
package com.ayoprez.sobuu.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.book.remote.BookResult
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.models.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val book: BookRepositoryImpl): ViewModel() {

    var state by mutableStateOf(SearchState())
    var booksList by mutableStateOf<List<Book>?>(emptyList())

    private val resultChannel = Channel<BookResult<List<Book>>>()
    val searchResult = resultChannel.receiveAsFlow()

    fun onEvent(event: SearchUIEvent) {
        when(event) {
            is SearchUIEvent.SearchTermChanged -> {
                state = state.copy(searchTerm = event.value)
            }
            is SearchUIEvent.removeErrorState -> TODO()
            is SearchUIEvent.searchTerm -> search()
            is SearchUIEvent.cleanSearchTerm -> {
                state = state.copy(searchTerm = "")
            }
        }
    }

//    fun handleError(error: AuthenticationError?) {
//        state = state.copy(error = error)
//    }

    private fun search() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.searchBook(
                term = state.searchTerm
            )

//            resultChannel.send(result)

            booksList = result.data

            state = state.copy(isLoading = false)
        }
    }
}
package com.ayoprez.sobuu.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.models.bo_models.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val book: BookRepositoryImpl): ViewModel() {

    var state by mutableStateOf(SearchState())
    var booksList by mutableStateOf<List<Book>?>(emptyList())

    fun onEvent(event: SearchUIEvent) {
        when(event) {
            is SearchUIEvent.SearchTermChanged -> {
                state = state.copy(
                    searchTerm = event.value,
                    language = event.lang,
                    searchFurther = state.searchFurther,
                )
            }
            is SearchUIEvent.removeErrorState -> TODO()
            is SearchUIEvent.searchTerm -> search()
            is SearchUIEvent.cleanSearchTerm -> {
                state = state.copy(
                    searchTerm = "",
                    language = "",
                )
            }
            is SearchUIEvent.searchFurther -> searchFurther()
        }
    }

    private fun handleError(error: BookError?) {
        state = state.copy(error = error)
    }

    private fun search() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.searchBook(
                term = state.searchTerm,
                language = state.language,
                searchFurther = false,
            )

            if(result.data.isNullOrEmpty()) {
                handleError(result.error)
            } else {
                booksList = result.data
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun searchFurther() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = book.searchBook(
                term = state.searchTerm,
                language = state.language,
                searchFurther = true,
            )

            if(result.data.isNullOrEmpty()) {
                handleError(result.error)
            } else {
                booksList = result.data
            }

            state = state.copy(isLoading = false)
        }
    }
}
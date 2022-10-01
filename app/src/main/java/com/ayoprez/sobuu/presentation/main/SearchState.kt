package com.ayoprez.sobuu.presentation.main

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class SearchState(
    val isLoading: Boolean = false,
    val searchTerm: String = "",
    val error: BookError? = null,
)
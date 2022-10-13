package com.ayoprez.sobuu.presentation.main

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class SearchState(
    val isLoading: Boolean = false,
    val searchTerm: String = "",
    val language: String = "en",
    val searchFurther: Boolean = false,
    val error: BookError? = null,
)
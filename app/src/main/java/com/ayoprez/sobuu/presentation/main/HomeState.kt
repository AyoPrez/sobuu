package com.ayoprez.sobuu.presentation.main

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class HomeState (
    val isLoading: Boolean = false,
    val isOnSearch: Boolean = false,
    val error: BookError? = null,
)
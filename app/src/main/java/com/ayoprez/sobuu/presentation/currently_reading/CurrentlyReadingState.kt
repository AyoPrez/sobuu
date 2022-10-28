package com.ayoprez.sobuu.presentation.currently_reading

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class CurrentlyReadingState(
    val isLoading: Boolean = false,
    val finished: Boolean = false,
    val gaveUp: Boolean = false,
    val progressUpdated: Boolean = false,
    val error: BookError? = null,
)
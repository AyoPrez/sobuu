package com.ayoprez.sobuu.presentation.book

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class BookState (
    val isLoading: Boolean = false,
    val error: BookError? = null,
    val displayStartedToRead: Boolean = false,
    val bookStartedReadingTitle: String? = null,
)
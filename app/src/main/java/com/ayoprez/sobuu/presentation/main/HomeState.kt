package com.ayoprez.sobuu.presentation.main

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class HomeState (
    val isLoading: Boolean = false,
    val isOnCurrentlyReading: Boolean = true,
    val isOnFinished: Boolean = false,
    val isOnGiveUp: Boolean = false,
    val error: BookError? = null,
)
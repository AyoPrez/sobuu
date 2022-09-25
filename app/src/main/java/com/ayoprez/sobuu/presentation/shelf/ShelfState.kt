package com.ayoprez.sobuu.presentation.shelf

import com.ayoprez.sobuu.shared.models.Shelf

data class ShelfState(
    val shelfList: List<Shelf>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

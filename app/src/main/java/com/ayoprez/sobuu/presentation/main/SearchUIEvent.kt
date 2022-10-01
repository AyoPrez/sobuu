package com.ayoprez.sobuu.presentation.main

sealed class SearchUIEvent {
    data class SearchTermChanged(val value: String): SearchUIEvent()
    object cleanSearchTerm: SearchUIEvent()
    object searchTerm: SearchUIEvent()
    object removeErrorState: SearchUIEvent()
}
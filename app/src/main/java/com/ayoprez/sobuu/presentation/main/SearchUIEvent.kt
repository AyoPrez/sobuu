package com.ayoprez.sobuu.presentation.main

sealed class SearchUIEvent {
    data class SearchTermChanged(val value: String, val lang: String): SearchUIEvent()
    object cleanSearchTerm: SearchUIEvent()
    object searchTerm: SearchUIEvent()
    object searchFurther: SearchUIEvent()
    object removeErrorState: SearchUIEvent()
}
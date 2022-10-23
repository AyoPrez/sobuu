package com.ayoprez.sobuu.presentation.main

sealed class HomeUIEvent {
    data class DisplaySearch(val value: Boolean): HomeUIEvent()
    object DisplayGiveUp: HomeUIEvent()
    object DisplayFinished: HomeUIEvent()
    object DisplayCurrentlyReading: HomeUIEvent()
}
package com.ayoprez.sobuu.presentation.main

sealed class HomeUIEvent {
    object displayCurrentlyReading: HomeUIEvent()
    object displayGiveUp: HomeUIEvent()
    object displayFinished: HomeUIEvent()
}
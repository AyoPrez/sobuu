package com.ayoprez.sobuu.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import com.ayoprez.sobuu.shared.models.bo_models.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val book: BookRepositoryImpl): ViewModel() {

    var state by mutableStateOf(HomeState())
    var currentlyReadingBooksList by mutableStateOf<List<Book>?>(emptyList())
    var finishedBooksList by mutableStateOf<List<Book>?>(emptyList())
    var giveUpBooksList by mutableStateOf<List<Book>?>(emptyList())

    fun onEvent(event: HomeUIEvent) {
        when(event) {
            HomeUIEvent.displayCurrentlyReading -> TODO()
            HomeUIEvent.displayFinished -> TODO()
            HomeUIEvent.displayGiveUp -> TODO()
        }
    }

}
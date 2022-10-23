package com.ayoprez.sobuu.presentation.currently_reading

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ayoprez.sobuu.shared.features.book.repository.BookRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CurrentlyReadingViewModel @Inject constructor(private val bookRepo: BookRepositoryImpl): ViewModel() {

    var state by mutableStateOf(CurrentlyReadingState())
}

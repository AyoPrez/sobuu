package com.ayoprez.sobuu.presentation.comments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ayoprez.sobuu.shared.features.comments.repository.CommentRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val commentsRepo: CommentRepositoryImpl): ViewModel() {

    var state by mutableStateOf(CommentsState())

    init {
        onEvent(CommentsUIEvents.displayCommentsScreen)
    }

    fun onEvent(event: CommentsUIEvents) {
        when(event) {
            is CommentsUIEvents.FilterByType -> {
                state = state.copy(applyFilter = true, filterType = event.type)
            }
            is CommentsUIEvents.displayCommentsScreen -> TODO()
            is CommentsUIEvents.displayFilterScreen -> TODO()
        }
    }

    private fun filterMessages() {

    }

}
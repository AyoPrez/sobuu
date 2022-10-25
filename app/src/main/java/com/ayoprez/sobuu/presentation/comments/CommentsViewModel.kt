package com.ayoprez.sobuu.presentation.comments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoprez.sobuu.shared.features.comments.remote.CommentError
import com.ayoprez.sobuu.shared.features.comments.repository.CommentRepositoryImpl
import com.ayoprez.sobuu.shared.models.bo_models.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val commentsRepo: CommentRepositoryImpl): ViewModel() {

    var state by mutableStateOf(CommentsState())
    var listOFComments by mutableStateOf<List<Comment>?>(emptyList())

    fun onEvent(event: CommentsUIEvents) {
        when(event) {
            is CommentsUIEvents.FilterByType -> {
                state = state.copy(applyFilter = true, filterType = event.type)
            }
            is CommentsUIEvents.DisplayCommentsScreen -> fetchCommentsForPageOrPercentage(
                bookId = event.bookId,
                page = event.page,
                percentage = event.percentage,
            )
            is CommentsUIEvents.DisplayFilterScreen -> TODO()
        }
    }

    fun handleError(error: CommentError?) {
        state = state.copy(error = error)
    }

    private fun filterMessages() {

    }

    private fun fetchCommentsForPageOrPercentage(bookId: String, page: Number? = null, percentage: Number? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = commentsRepo.getCommentsInPageOrPercentage(
                bookId = bookId,
                page = page,
                percentage = percentage,
            )

            if(result.error != null) {
                handleError(result.error)
            } else {
                handleError(null)
                listOFComments = result.data ?: emptyList()
            }

            state = state.copy(isLoading = false)
        }
    }

}
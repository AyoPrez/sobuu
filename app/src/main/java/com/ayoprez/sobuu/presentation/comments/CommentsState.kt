package com.ayoprez.sobuu.presentation.comments

import com.ayoprez.sobuu.shared.features.book.remote.BookError

data class CommentsState(
    val isLoading: Boolean = false,
    val applyFilter: Boolean = false,
    val filterType: CommentFilterType? = null,
    val error: BookError? = null,
)
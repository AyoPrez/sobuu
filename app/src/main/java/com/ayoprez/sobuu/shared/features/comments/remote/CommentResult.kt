package com.ayoprez.sobuu.shared.features.comments.remote


sealed class CommentResult<T>(val data: T? = null, val error: CommentError? = null) {
    class Success<T>(data: T? = null) : CommentResult<T>(data = data)
    class Error<T>(error: CommentError? = null) : CommentResult<T>(error = error)
}

sealed class CommentError {
    object UnauthorizedQueryError : CommentError()
    object InvalidSessionTokenError : CommentError()
    object InvalidBookIdError : CommentError()
    object InvalidCommentIdError : CommentError()
    object EmptyFieldError : CommentError()
    object InvalidPageNumberError : CommentError()
    object InvalidPercentageNumberError : CommentError()
    object InvalidDoublePageAndPercentageNumberError : CommentError()
    object ProcessingQueryError : CommentError()
    object TimeOutError : CommentError()
    object UnknownError : CommentError()
}
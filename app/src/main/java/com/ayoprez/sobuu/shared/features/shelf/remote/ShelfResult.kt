package com.ayoprez.sobuu.shared.features.shelf.remote

sealed class ShelfResult<T>(val data: T? = null, val error: ShelfError? = null) {
    class Success<T>(data: T? = null) : ShelfResult<T>(data = data)
    class Error<T>(error: ShelfError? = null) : ShelfResult<T>(error = error)
}

sealed class ShelfError {
    object UnauthorizedQueryError : ShelfError()
    object InvalidSessionTokenError : ShelfError()
    object EmptyTerm : ShelfError()
    object EmptyShelfId : ShelfError()
    object EmptyBookId : ShelfError()
    object InvalidShelfId : ShelfError()
    object EmptyName : ShelfError()
    object EmptyDescription : ShelfError()
    object ProcessingQueryError : ShelfError()
    object TimeOutError : ShelfError()
    object UnknownError : ShelfError()
}
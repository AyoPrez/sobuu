package com.ayoprez.sobuu.shared.features.book.remote

sealed class BookResult<T>(val data: T? = null, val error: BookError? = null) {
    class Success<T>(data: T? = null) : BookResult<T>(data = data)
    class Error<T>(error: BookError? = null) : BookResult<T>(error = error)
}

sealed class BookError {
    object UnauthorizedQueryError : BookError()
    object InvalidSessionTokenError : BookError()
    object EmptySearchTermError : BookError()
    object InvalidBookIdError : BookError()
    object InvalidRateIdError : BookError()
    object InvalidPageNumberError : BookError()
    object InvalidRateNumberError : BookError()
    object InvalidPercentageNumberError : BookError()
    object InvalidFinishedAndGiveUpBookValuesError : BookError()
    object InvalidDoubleValueError : BookError()
    object ProcessingQueryError : BookError()
    object TimeOutError : BookError()
    object UnknownError : BookError()
}
package com.ayoprez.sobuu.shared.features.profile.remote

sealed class ProfileResult<T>(val data: T? = null, val error: ProfileError? = null) {
    class Success<T>(data: T? = null) : ProfileResult<T>(data = data)
    class Error<T>(error: ProfileError? = null) : ProfileResult<T>(error = error)
}

sealed class ProfileError {
    object UnauthorizedQueryError : ProfileError()
    object InvalidSessionTokenError : ProfileError()
    object ProcessingQueryError : ProfileError()
    object TimeOutError : ProfileError()
    object UnknownError : ProfileError()
}
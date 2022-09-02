package com.ayoprez.sobuu.shared.features.authentication.remote

sealed class AuthenticationResult<T> (val data: T? = null, val error: AuthenticationError? = null) {
    class Authorized<T>(data: T? = null) : AuthenticationResult<T>(data = data)
    class LoggedOut<T> : AuthenticationResult<T>()
    class ResetPassword<T> : AuthenticationResult<T>()
    class Unauthorized<T> : AuthenticationResult<T>()
    class Registered<T> : AuthenticationResult<T>()
    class Error<T>(error: AuthenticationError? = null): AuthenticationResult<T>(error = error)
}

sealed class AuthenticationError {
    object InvalidSessionToken: AuthenticationError()
    object InvalidCredentials: AuthenticationError()
    object EmptyCredentialsError: AuthenticationError()
    object TimeOutError: AuthenticationError()
    object InvalidEmailError: AuthenticationError()
    object UsernameAlreadyTaken: AuthenticationError()
    object EmailAlreadyTaken: AuthenticationError()
    object WrongEmailFormatError: AuthenticationError()
    object UnknownError: AuthenticationError()
}
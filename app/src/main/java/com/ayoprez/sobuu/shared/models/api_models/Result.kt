package com.ayoprez.sobuu.shared.models.api_models

data class Result(
    val bookProgress: List<BookProgress>,
    val firstName: String,
//    val following: List<Result>,
    val id: String,
    val lastName: String,
    val userShelves: List<UserShelf>
)
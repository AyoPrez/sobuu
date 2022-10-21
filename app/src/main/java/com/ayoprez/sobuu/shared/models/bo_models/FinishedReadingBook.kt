package com.ayoprez.sobuu.shared.models.bo_models

data class FinishedReadingBook (
    val id: String,
    val title: String,
    val authors: List<String>,
    val picture: String,
    val bookProgress: BookProgress? = null,
    val bookProgressComments: List<Comment>? = null,
    val userRating: UserBookRating? = null
)
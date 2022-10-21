package com.ayoprez.sobuu.shared.models.bo_models

data class CurrentlyReadingBook(
    val id: String,
    val title: String,
    val authors: List<String>,
    val picture: String,
    val bookProgress: BookProgress? = null,
    val bookProgressComments: List<Comment>? = null,
)

package com.ayoprez.sobuu.shared.models.api_models

data class UserShelf(
    val books: List<Any>,
    val id: String,
    val isPublic: Boolean,
    val name: String,
    val description: String?
)
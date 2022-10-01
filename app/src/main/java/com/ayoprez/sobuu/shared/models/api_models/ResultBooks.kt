package com.ayoprez.sobuu.shared.models.api_models

data class ResultBooks(
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String,
    val publishedDate: String,
    val publisher: String,
    val picture: String,
    val credits: List<String>,
    val totalPages: Int,
    val genres: List<String>,
    val isbn: List<String>
)

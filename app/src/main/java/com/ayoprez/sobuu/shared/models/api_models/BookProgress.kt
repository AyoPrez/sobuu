package com.ayoprez.sobuu.shared.models.api_models

data class BookProgress(
    val finished: Boolean,
    val giveUp: Boolean,
    val id: String,
    val page: Int,
    val percentage: Int
)
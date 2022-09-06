package com.ayoprez.sobuu.shared.models

data class BookProgress(val id: String,
                val percentage: Number?,
                val page: Number?,
                val finished: Boolean,
                val giveUp: Boolean,
)
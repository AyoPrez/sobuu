package com.ayoprez.sobuu.shared.models

data class Shelf(val id: String,
                 val books: List<Book>,
                 val name: String,
                 val description: String,
                 val isPublic: Boolean)

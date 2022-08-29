package com.ayoprez.sobuu.shared.models


data class Book(val id: String,
                val title: String,
                val authors: List<String>,
                val description: String,
                val picture: String,
                val publisher: String,
                val credits: List<String>,
                val totalPages: Int,
                val isbn: List<String>,
                val publishedDate: String,
                val bookComments: List<Comment>,
                val bookRating:List<UserBookRating>)
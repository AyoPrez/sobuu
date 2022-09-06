package com.ayoprez.sobuu.shared.models

data class UserBookRating(val id: String,
                          val book: Book,
                          val user: Profile,
                          val rating: Double,
                          val review: String)

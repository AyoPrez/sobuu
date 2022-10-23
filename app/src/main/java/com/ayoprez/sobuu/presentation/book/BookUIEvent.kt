package com.ayoprez.sobuu.presentation.book

sealed class BookUIEvent {
    object DisplayUserShelves: BookUIEvent()
    object CancelAllErrors: BookUIEvent()
    data class AddBookToShelf(val bookId: String, val shelfId: String): BookUIEvent()
    data class StartToReadBook(val bookId: String, val bookTitle: String): BookUIEvent()
}
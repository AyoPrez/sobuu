package com.ayoprez.sobuu.presentation.currently_reading


sealed class CurrentlyReadingUIEvent {
    data class UpdateProgress(val bookID: String, val page: Int? = null, val percentage: Int? = null): CurrentlyReadingUIEvent()
    data class UpdateProgressChanged(val page: Int? = null, val percentage: Int? = null): CurrentlyReadingUIEvent()
    data class FetchBookProgressData(val bookId: String): CurrentlyReadingUIEvent()
    data class GiveUpBook(val bookID: String): CurrentlyReadingUIEvent()
    data class FinishBook(val bookID: String): CurrentlyReadingUIEvent()
}
package com.ayoprez.sobuu.shared.features.book.remote

import com.ayoprez.sobuu.shared.models.Book
import com.ayoprez.sobuu.shared.models.BookProgress
import com.ayoprez.sobuu.shared.models.Comment
import com.ayoprez.sobuu.shared.models.UserBookRating

interface IBookRemoteData {

    suspend fun getUserCurrentReadingBook(sessionToken: String?) : BookResult<List<Book>>

    suspend fun searchBook(sessionToken: String?, term: String) : BookResult<List<Book>>

    suspend fun getCommentsFromPage(sessionToken: String?, bookId: String, pageNum: Number) : BookResult<List<Comment>>

    suspend fun getCommentsFromPercentage(sessionToken: String?, bookId: String, percentage: Number) : BookResult<List<Comment>>

    suspend fun setBookCurrentlyReading(sessionToken: String?, bookId: String) : BookResult<Unit>

    suspend fun getBookProgress(sessionToken: String?, bookId: String) : BookResult<BookProgress>

    suspend fun updateBookProgress(sessionToken: String?, bookId: String, percentage: Number?,
                                   page: Number?, finished: Boolean, giveUp: Boolean) : BookResult<Unit>

    suspend fun rateBook(sessionToken: String?, bookId: String, rate: Double, reviewText: String) : BookResult<Book>

    suspend fun getUserRatingInBook(sessionToken: String?, bookId: String) : BookResult<UserBookRating>

    suspend fun getRatingListFromBook(sessionToken: String?, bookId: String) : BookResult<List<UserBookRating>>

    suspend fun removeRating(sessionToken: String?, rateId: String) : BookResult<Unit>

    suspend fun finishBook(sessionToken: String?, bookId: String) : BookResult<BookProgress>

    suspend fun giveUpWithBook(sessionToken: String?, bookId: String) : BookResult<BookProgress>
}
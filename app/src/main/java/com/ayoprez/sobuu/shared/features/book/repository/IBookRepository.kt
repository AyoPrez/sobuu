package com.ayoprez.sobuu.shared.features.book.repository

import com.ayoprez.sobuu.shared.features.book.remote.BookResult
import com.ayoprez.sobuu.shared.models.bo_models.*

interface IBookRepository {
    suspend fun getUserCurrentReadingBook() : BookResult<List<CurrentlyReadingBook>>

    suspend fun getUserFinishedReadingBook() : BookResult<List<FinishedReadingBook>>

    suspend fun getUserGiveUpBook() : BookResult<List<GiveUpBook>>

    suspend fun searchBook(term: String, language: String, searchFurther: Boolean) : BookResult<List<Book>>

    suspend fun getCommentsFromPage(bookId: String, pageNum: Number) : BookResult<List<Comment>>

    suspend fun getCommentsFromPercentage(bookId: String, percentage: Number) : BookResult<List<Comment>>

    suspend fun setBookCurrentlyReading(bookId: String) : BookResult<Unit>

    suspend fun getBookProgress(bookId: String) : BookResult<BookProgress>

    suspend fun updateBookProgress(bookId: String, percentage: Number?,
                                   page: Number?, finished: Boolean, giveUp: Boolean) : BookResult<Unit>

    suspend fun rateBook(bookId: String, rate: Double, reviewText: String) : BookResult<Book>

    suspend fun getUserRatingInBook(bookId: String) : BookResult<UserBookRating>

    suspend fun getRatingListFromBook(bookId: String) : BookResult<List<UserBookRating>>

    suspend fun removeRating(rateId: String) : BookResult<Unit>

    suspend fun finishBook(bookId: String) : BookResult<BookProgress>

    suspend fun giveUpWithBook(bookId: String) : BookResult<BookProgress>

}
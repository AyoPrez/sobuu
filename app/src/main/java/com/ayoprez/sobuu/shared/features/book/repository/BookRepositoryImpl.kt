package com.ayoprez.sobuu.shared.features.book.repository

import com.ayoprez.sobuu.shared.features.book.database.IBookLocalData
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.remote.BookResult
import com.ayoprez.sobuu.shared.features.book.remote.IBookRemoteData
import com.ayoprez.sobuu.shared.models.bo_models.*
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookRemoteData: IBookRemoteData,
    private val bookLocalData: IBookLocalData
): IBookRepository {
    override suspend fun getUserCurrentReadingBook(): BookResult<List<CurrentlyReadingBook>> = execute {
        bookRemoteData.getUserCurrentReadingBook(it)
    }

    override suspend fun getUserFinishedReadingBook(): BookResult<List<FinishedReadingBook>> = execute {
        bookRemoteData.getUserAlreadyReadBooks(it)
    }

    override suspend fun getUserGiveUpBook(): BookResult<List<GiveUpBook>> = execute {
        bookRemoteData.getUserGiveUpBooks(it)
    }

    override suspend fun searchBook(term: String, language: String, searchFurther: Boolean): BookResult<List<Book>> = execute {
        bookRemoteData.searchBook(
            sessionToken = it,
            term = term,
            language = language,
            searchFurther = searchFurther,
        )
    }

    override suspend fun getCommentsFromPage(
        bookId: String,
        pageNum: Number
    ): BookResult<List<Comment>> = execute {
       bookRemoteData.getCommentsFromPage(
           sessionToken = it,
           bookId = bookId,
           pageNum = pageNum,
       )
    }

    override suspend fun getCommentsFromPercentage(
        bookId: String,
        percentage: Number
    ): BookResult<List<Comment>> = execute {
        bookRemoteData.getCommentsFromPercentage(
            sessionToken = it,
            bookId = bookId,
            percentage = percentage,
        )
    }

    override suspend fun setBookCurrentlyReading(bookId: String): BookResult<Unit> = execute {
        bookRemoteData.setBookCurrentlyReading(
            sessionToken = it,
            bookId = bookId,
        )
    }

    override suspend fun getBookProgress(bookId: String): BookResult<CurrentlyReadingBook> = execute {
        bookRemoteData.getBookProgress(
            sessionToken = it,
            bookId = bookId,
        )
    }

    override suspend fun updateBookProgress(
        bookId: String,
        percentage: Number?,
        page: Number?,
        finished: Boolean,
        giveUp: Boolean
    ): BookResult<BookProgress> = execute {
        bookRemoteData.updateBookProgress(
            sessionToken = it,
            bookId = bookId,
            percentage = percentage,
            page = page,
            finished = finished,
            giveUp = giveUp,
        )
    }

    override suspend fun rateBook(
        bookId: String,
        rate: Double,
        reviewText: String
    ): BookResult<Book> = execute {
        bookRemoteData.rateBook(
            sessionToken = it,
            bookId = bookId,
            rate = rate,
            reviewText = reviewText,
        )
    }

    override suspend fun getUserRatingInBook(bookId: String): BookResult<UserBookRating> = execute {
        bookRemoteData.getUserRatingInBook(
            sessionToken = it,
            bookId = bookId,
        )
    }

    override suspend fun getRatingListFromBook(bookId: String): BookResult<List<UserBookRating>> = execute {
        bookRemoteData.getRatingListFromBook(
            sessionToken = it,
            bookId = bookId,
        )
    }

    override suspend fun removeRating(rateId: String): BookResult<Unit> = execute {
        bookRemoteData.removeRating(
            sessionToken = it,
            rateId = rateId,
        )
    }

    override suspend fun finishBook(bookId: String): BookResult<BookProgress> = execute {
        bookRemoteData.finishBook(
            sessionToken = it,
            bookId = bookId,
        )
    }

    override suspend fun giveUpWithBook(bookId: String): BookResult<BookProgress> = execute {
        bookRemoteData.giveUpWithBook(
            sessionToken = it,
            bookId = bookId,
        )
    }

    private suspend fun <T>execute(func: suspend (sessionToken: String) -> BookResult<T>) : BookResult<T> {
        val sessionToken = bookLocalData.getSessionToken() ?: return BookResult.Error(
            BookError.InvalidSessionTokenError)
        return func(sessionToken)
    }
}
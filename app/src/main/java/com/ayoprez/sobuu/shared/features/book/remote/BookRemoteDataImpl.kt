package com.ayoprez.sobuu.shared.features.book.remote

import com.ayoprez.sobuu.shared.models.Book
import com.ayoprez.sobuu.shared.models.BookProgress
import com.ayoprez.sobuu.shared.models.Comment
import com.ayoprez.sobuu.shared.models.UserBookRating
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class BookRemoteDataImpl @Inject constructor(
    private val api: BookApi
): IBookRemoteData {

    override suspend fun getUserCurrentReadingBook(sessionToken: String?): BookResult<List<Book>> = execute(sessionToken) {
        api.getUserCurrentReadingBook(
            sessionToken = it
        )
    }

    override suspend fun searchBook(sessionToken: String?, term: String): BookResult<List<Book>> {
        if (term.isBlank()) return BookResult.Error(BookError.EmptySearchTermError)
        return execute(sessionToken) {
            api.searchBook(
                sessionToken = it,
                term = term,
            )
        }
    }

    override suspend fun getCommentsFromPage(
        sessionToken: String?,
        bookId: String,
        pageNum: Number
    ): BookResult<List<Comment>> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if(pageNum.toInt() < 0) return BookResult.Error(BookError.InvalidPageNumberError)
        return execute(sessionToken) {
            api.getCommentsFromPage(
                sessionToken = it,
                bookId = bookId,
                pageNum = pageNum
            )
        }
    }

    override suspend fun getCommentsFromPercentage(
        sessionToken: String?,
        bookId: String,
        percentage: Number
    ): BookResult<List<Comment>> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if(percentage.toInt() < 0 || percentage.toInt() > 100) return BookResult.Error(BookError.InvalidPercentageNumberError)
        return execute(sessionToken) {
            api.getCommentsFromPercentage(
                sessionToken = it,
                bookId = bookId,
                percentage = percentage
            )
        }
    }

    override suspend fun setBookCurrentlyReading(
        sessionToken: String?,
        bookId: String
    ): BookResult<Unit> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.setBookAsCurrentlyReading(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    override suspend fun getBookProgress(
        sessionToken: String?,
        bookId: String
    ): BookResult<BookProgress> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.getBookProgress(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    override suspend fun updateBookProgress(
        sessionToken: String?,
        bookId: String,
        percentage: Number?,
        page: Number?,
        finished: Boolean,
        giveUp: Boolean
    ): BookResult<Unit> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if(percentage == null && page == null) return BookResult.Error(BookError.InvalidPageNumberError)
        if(percentage != null && page != null) return BookResult.Error(BookError.InvalidDoubleValueError)
        if(page != null && page.toInt() < 0) return BookResult.Error(BookError.InvalidPageNumberError)
        if(percentage != null && (percentage.toInt() > 100 || percentage.toInt() < 0)) return BookResult.Error(BookError.InvalidPercentageNumberError)
        if(finished && giveUp) return BookResult.Error(BookError.InvalidFinishedAndGiveUpBookValuesError)
        return execute(sessionToken) {
            api.updateProgressBook(
                sessionToken = it,
                bookId = bookId,
                percentage = percentage,
                page = page,
                finished = finished,
                giveUp = giveUp
            )
        }
    }

    override suspend fun rateBook(
        sessionToken: String?,
        bookId: String,
        rate: Double,
        reviewText: String
    ): BookResult<Book> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if(rate < 0 || rate > 10) return BookResult.Error(BookError.InvalidRateNumberError)
        return execute(sessionToken) {
            api.ratingBook(
                sessionToken = it,
                bookId = bookId,
                rating = rate,
                reviewText = reviewText
            )
        }
    }

    override suspend fun getUserRatingInBook(
        sessionToken: String?,
        bookId: String
    ): BookResult<UserBookRating> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.getUserRatingInBook(
                sessionToken = it,
                bookId = bookId
            )
        }
    }

    override suspend fun getRatingListFromBook(
        sessionToken: String?,
        bookId: String
    ): BookResult<List<UserBookRating>> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.getRatingListFromBook(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    override suspend fun removeRating(sessionToken: String?, rateId: String): BookResult<Unit> {
        if(rateId.isBlank()) return BookResult.Error(BookError.InvalidRateIdError)
        return execute(sessionToken) {
            api.removeRating(
                sessionToken = it,
                ratingId = rateId,
            )
        }
    }

    override suspend fun finishBook(
        sessionToken: String?,
        bookId: String
    ): BookResult<BookProgress> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.finishBook(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    override suspend fun giveUpWithBook(
        sessionToken: String?,
        bookId: String
    ): BookResult<BookProgress> {
        if(bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.giveUpWithBook(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    private suspend fun <T>execute(sessionToken: String?, func: suspend (sessionToken: String) -> Response<T>): BookResult<T> {
        return try {
            if (sessionToken.isNullOrBlank()) return BookResult.Error(BookError.InvalidSessionTokenError)

            val result = func(sessionToken)

            if (result.body() == null && result.errorBody() != null) return handleResponseError(
                result.errorBody()
            )

            return BookResult.Success(data = result.body())
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> BookResult.Error(BookError.UnauthorizedQueryError)
                209 -> BookResult.Error(BookError.InvalidSessionTokenError)
                else -> BookResult.Error(BookError.UnknownError)
            }
        } catch (e: Exception) {
            BookResult.Error(BookError.UnknownError)
        }
    }

    private fun <T>handleResponseError(errorBody: ResponseBody?): BookResult<T> {
        if (errorBody == null) return BookResult.Error(error = BookError.UnknownError)
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when(it) {
                    101 -> BookResult.Error(BookError.UnauthorizedQueryError)
                    141 -> BookResult.Error(BookError.ProcessingQueryError)
                    124 -> BookResult.Error(BookError.TimeOutError)
                    209 -> BookResult.Error(BookError.InvalidSessionTokenError)
                    else -> BookResult.Error(BookError.UnknownError)
                }
            }
    }
}
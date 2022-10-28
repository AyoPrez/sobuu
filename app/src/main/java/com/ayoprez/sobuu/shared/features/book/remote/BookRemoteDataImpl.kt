package com.ayoprez.sobuu.shared.features.book.remote

import com.ayoprez.sobuu.shared.models.api_models.books_api.AllReview
import com.ayoprez.sobuu.shared.models.api_models.books_api.BooksApi
import com.ayoprez.sobuu.shared.models.api_models.books_api.User
import com.ayoprez.sobuu.shared.models.api_models.books_api.UserRating
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.BookProgressApi
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.CurrentlyReadingBookApi
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.Extras
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.UpdateProgressResultApi
import com.ayoprez.sobuu.shared.models.api_models.finished_books_api.FinishedBooksApi
import com.ayoprez.sobuu.shared.models.api_models.give_up_books_api.GiveUpBooksApi
import com.ayoprez.sobuu.shared.models.bo_models.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalDateTime
import javax.inject.Inject
import com.ayoprez.sobuu.shared.models.api_models.finished_books_api.Extras as finishedBookExtras
import com.ayoprez.sobuu.shared.models.api_models.give_up_books_api.Extras as giveUpBooksExtras

class BookRemoteDataImpl @Inject constructor(
    private val api: BookApi
) : IBookRemoteData {

    override suspend fun getUserCurrentReadingBook(sessionToken: String?): BookResult<List<CurrentlyReadingBook>> {
        val result = execute(sessionToken) {
            api.getUserCurrentReadingBook(
                sessionToken = it
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.toBookList())
        } else {
            BookResult.Error(error = result.error)
        }
    }

    override suspend fun getUserAlreadyReadBooks(sessionToken: String?): BookResult<List<FinishedReadingBook>> {
        val result = execute(sessionToken) {
            api.getUserAlreadyReadBooks(
                sessionToken = it
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.toBookList())
        } else {
            BookResult.Error(error = result.error)
        }
    }

    override suspend fun getUserGiveUpBooks(sessionToken: String?): BookResult<List<GiveUpBook>> {
        val result = execute(sessionToken) {
            api.getUserGiveUpBooks(
                sessionToken = it
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.toBookList())
        } else {
            BookResult.Error(error = result.error)
        }
    }

    override suspend fun searchBook(
        sessionToken: String?,
        term: String,
        language: String,
        searchFurther: Boolean
    ): BookResult<List<Book>> {
        if (term.isBlank()) return BookResult.Error(BookError.EmptySearchTermError)

        val result = execute(sessionToken) {
            api.searchBook(
                sessionToken = it,
                term = term,
                lang = language,
                searchFurther = searchFurther,
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.toBookList())
        } else {
            BookResult.Error(error = result.error)
        }
    }

    override suspend fun getCommentsFromPage(
        sessionToken: String?,
        bookId: String,
        pageNum: Number
    ): BookResult<List<Comment>> {
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if (pageNum.toInt() < 0) return BookResult.Error(BookError.InvalidPageNumberError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if (percentage.toInt() < 0 || percentage.toInt() > 100) return BookResult.Error(BookError.InvalidPercentageNumberError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
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
    ): BookResult<CurrentlyReadingBook> {
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)

        val result = execute(sessionToken) {
            api.getBookProgress(
                sessionToken = it,
                bookId = bookId,
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.toCurrentlyReadingBook())
        } else {
            BookResult.Error(error = result.error)
        }

    }

    override suspend fun updateBookProgress(
        sessionToken: String?,
        bookId: String,
        percentage: Number?,
        page: Number?,
        finished: Boolean,
        giveUp: Boolean
    ): BookResult<BookProgress> {
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if (percentage == null && page == null) return BookResult.Error(BookError.InvalidPageNumberError)
        if (percentage != null && page != null) return BookResult.Error(BookError.InvalidDoubleValueError)
        if (page != null && page.toInt() < 0) return BookResult.Error(BookError.InvalidPageNumberError)
        if (percentage != null && (percentage.toInt() > 100 || percentage.toInt() < 0)) return BookResult.Error(
            BookError.InvalidPercentageNumberError
        )
        if (finished && giveUp) return BookResult.Error(BookError.InvalidFinishedAndGiveUpBookValuesError)

        val result = execute(sessionToken) {
            api.updateProgressBook(
                sessionToken = it,
                bookId = bookId,
                percentage = percentage,
                page = page,
                finished = finished,
                giveUp = giveUp
            )
        }

        return if (result.data != null) {
            BookResult.Success(data = result.data.result.extras.toBookProgress())
        } else {
            BookResult.Error(error = result.error)
        }
    }

    override suspend fun rateBook(
        sessionToken: String?,
        bookId: String,
        rate: Double,
        reviewText: String
    ): BookResult<Book> {
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        if (rate < 0 || rate > 10) return BookResult.Error(BookError.InvalidRateNumberError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.getRatingListFromBook(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    override suspend fun removeRating(sessionToken: String?, rateId: String): BookResult<Unit> {
        if (rateId.isBlank()) return BookResult.Error(BookError.InvalidRateIdError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
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
        if (bookId.isBlank()) return BookResult.Error(BookError.InvalidBookIdError)
        return execute(sessionToken) {
            api.giveUpWithBook(
                sessionToken = it,
                bookId = bookId,
            )
        }
    }

    private suspend fun <T> execute(
        sessionToken: String?,
        func: suspend (sessionToken: String) -> Response<T>
    ): BookResult<T> {
        return try {
            if (sessionToken.isNullOrBlank()) return BookResult.Error(BookError.InvalidSessionTokenError)

            val result = func(sessionToken)

            println("-----Error: ${result.errorBody().toString()}")
            if (result.body() == null && result.errorBody() != null) return handleResponseError(
                result.errorBody()
            )

            return BookResult.Success(data = result.body())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> BookResult.Error(BookError.UnauthorizedQueryError)
                209 -> BookResult.Error(BookError.InvalidSessionTokenError)
                else -> BookResult.Error(BookError.UnknownError)
            }
        } catch (e: Exception) {
            println("---Error: $e")
            BookResult.Error(BookError.UnknownError)
        }
    }

    private fun <T> handleResponseError(errorBody: ResponseBody?): BookResult<T> {
        if (errorBody == null) return BookResult.Error(error = BookError.UnknownError)
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when (it) {
                    101 -> BookResult.Error(BookError.UnauthorizedQueryError)
                    141 -> BookResult.Error(BookError.ProcessingQueryError)
                    124 -> BookResult.Error(BookError.TimeOutError)
                    209 -> BookResult.Error(BookError.InvalidSessionTokenError)
                    else -> BookResult.Error(BookError.UnknownError)
                }
            }
    }

    // region Converters
    private fun BooksApi.toBookList(): List<Book> {
        return this.result.map {
            Book(
                id = it.id ?: "",
                title = it.title,
                authors = it.authors,
                description = it.description,
                totalPages = it.totalPages,
                publishedDate = it.publishedDate,
                publisher = it.publisher,
                picture = it.picture,
                thumbnail = it.thumbnail,
                isbn = it.isbn,
                genres = it.genres,
                credits = it.credits,
                totalComments = it.extras?.totalComments ?: 0,
                peopleReadingIt = it.extras?.peopleReadingIt ?: 0,
                readingStatus = when (it.extras?.readingStatus) {
                    1 -> BookReadingStatus.READING
                    2 -> BookReadingStatus.FINISHED
                    3 -> BookReadingStatus.GIVE_UP
                    else -> BookReadingStatus.NOT_READ
                },
                allReviews = it.extras?.allReviews?.toUserBookRatingList() ?: emptyList(),
                userRating = it.extras?.userRating?.toUserBookRating(null),
                totalRating = it.extras?.totalRating ?: 0.0,
            )
        }
    }

    private fun List<AllReview>.toUserBookRatingList(): List<UserBookRating> {
        return this.map { it.toUserBookRating(null) }
    }

    private fun AllReview.toUserBookRating(book: Book?): UserBookRating {
        return UserBookRating(
            id = this.id,
            book = book,
            review = this.review,
            rating = this.rating,
            user = this.user.toProfile(),
            date = LocalDateTime.parse(this.date.iso.substring(0, this.date.iso.length - 1))
        )
    }

    private fun UserRating.toUserBookRating(book: Book?): UserBookRating {
        return UserBookRating(
            id = this.id,
            book = book,
            review = this.review,
            rating = this.rating,
            user = this.user.toProfile(),
            date = LocalDateTime.parse(this.date.iso.substring(0, this.date.iso.length - 1))
        )
    }

    private fun User.toProfile(): Profile {
        return Profile(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
        )
    }

    private fun CurrentlyReadingBookApi.toBookList(): List<CurrentlyReadingBook> {
        return this.result.map {
            CurrentlyReadingBook(
                id = it.id,
                title = it.title,
                authors = it.authors,
                picture = it.picture,
                bookProgress = it.extras.toBookProgress(),
                bookProgressComments = it.extras.toBookProgressComments(
                    it.extras.currentPage,
                    it.extras.currentPercentage?.toByte()
                ),
            )
        }
    }

    private fun Extras.toBookProgress(): BookProgress {
        return BookProgress(
            percentage = this.currentPercentage,
            page = this.currentPage,
            progressInPercentage = this.currentProgress,
            startedToRead = LocalDateTime.parse(
                this.startedToRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
        )
    }

    private fun Extras.toBookProgressComments(page: Int?, percentage: Byte?): List<Comment> {
        return this.commentsInProgress.map {
            Comment(
                id = it.id,
                username = it.username,
                text = it.text,
                hasSpoilers = it.hasSpoilers,
                votesCounter = it.votes.toLong(),
                publishedDate = LocalDateTime.parse(
                    it.date.iso.substring(
                        0,
                        it.date.iso.length - 1
                    )
                ),
                percentage = percentage,
                pageNumber = page,
            )
        }
    }

    private fun FinishedBooksApi.toBookList(): List<FinishedReadingBook> {
        return this.result.map {
            FinishedReadingBook(
                id = it.id,
                title = it.title,
                authors = it.authors,
                picture = it.picture,
                bookProgress = it.extras.toBookProgress(),
                bookProgressComments = it.extras.toBookProgressComments(),
                userRating = it.extras.toUserRating(),
            )
        }
    }

    private fun finishedBookExtras.toBookProgress(): BookProgress {
        return BookProgress(
            percentage = this.currentPercentage,
            progressInPercentage = this.currentProgress,
            startedToRead = LocalDateTime.parse(
                this.startedToRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
            finishedToRead = LocalDateTime.parse(
                this.finishedToRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
            finished = true,
            page = null,
        )
    }

    private fun finishedBookExtras.toBookProgressComments(): List<Comment> {
        return this.commentsInProgress.map {
            Comment(
                id = it.id,
                username = it.username,
                text = it.text,
                hasSpoilers = it.hasSpoilers,
                votesCounter = it.votes.toLong(),
                publishedDate = LocalDateTime.parse(
                    it.date.iso.substring(
                        0,
                        it.date.iso.length - 1
                    )
                ),
                percentage = 100,
                pageNumber = null,
            )
        }
    }

    private fun finishedBookExtras.toUserRating(): UserBookRating {
        return UserBookRating(
            id = this.userBookRating.id,
            rating = this.userBookRating.rate,
            date = LocalDateTime.parse(
                this.userBookRating.date.iso.substring(
                    0,
                    this.userBookRating.date.iso.length - 1
                )
            ),
            review = this.userBookRating.review ?: "",
        )
    }

    private fun GiveUpBooksApi.toBookList(): List<GiveUpBook> {
        return this.result.map {
            GiveUpBook(
                id = it.id,
                title = it.title,
                authors = it.authors,
                picture = it.picture,
                bookProgress = it.extras.toBookProgress(),
                bookProgressComments = it.extras.toBookProgressComments(),
            )
        }
    }

    private fun UpdateProgressResultApi.toCurrentlyReadingBook(): CurrentlyReadingBook {
        val page = this.result.extras.currentPage
        val percentage = this.result.extras.currentPercentage
        return CurrentlyReadingBook(
            id = this.result.id,
            title = this.result.title,
            authors = this.result.authors,
            picture = this.result.picture,
            bookProgress = this.result.extras.toBookProgress(),
            bookProgressComments = this.result.extras.toBookProgressComments(
                page = page,
                percentage = percentage?.toByte()
            ),
        )
    }

    private fun giveUpBooksExtras.toBookProgress(): BookProgress {
        return BookProgress(
            percentage = this.currentPercentage,
            progressInPercentage = this.currentProgress,
            startedToRead = LocalDateTime.parse(
                this.startedToRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
            finishedToRead = LocalDateTime.parse(
                this.gaveUpRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
            finished = false,
            giveUp = true,
            page = this.currentPage,
        )
    }

    private fun giveUpBooksExtras.toBookProgressComments(): List<Comment> {
        return this.commentsInProgress.map {
            Comment(
                id = it.id,
                username = it.username,
                text = it.text,
                hasSpoilers = it.hasSpoilers,
                votesCounter = it.votes.toLong(),
                publishedDate = LocalDateTime.parse(
                    it.date.iso.substring(
                        0,
                        it.date.iso.length - 1
                    )
                ),
                percentage = 100,
                pageNumber = null,
            )
        }
    }

    private fun BookProgressApi.toBookProgress(): BookProgress {
        return BookProgress(
            id = this.id,
            page = this.page,
            percentage = this.percentage,
            finished = this.finished,
            giveUp = this.giveUp,
            progressInPercentage = this.progressInPercentage,
            finishedToRead = if(this.finishedToRead?.iso != null) LocalDateTime.parse(
                this.finishedToRead.iso.substring(
                    0,
                    this.finishedToRead.iso.length - 1
                )
            ) else null,
            startedToRead = LocalDateTime.parse(
                this.startedToRead.iso.substring(
                    0,
                    this.startedToRead.iso.length - 1
                )
            ),
        )
    }

    // endregion
}
package com.ayoprez.sobuu.shared.features

import com.ayoprez.sobuu.shared.models.api_models.books_api.BooksApi
import com.ayoprez.sobuu.shared.models.api_models.books_api.Extras
import com.ayoprez.sobuu.shared.models.api_models.books_api.Result
import com.ayoprez.sobuu.shared.models.bo_models.*
import java.time.LocalDateTime

object Utils {

    val bookProgress = BookProgress(
        id = "ewois98",
        percentage = null,
        page = 33,
        finished = false,
        giveUp = false,
        startedToRead = LocalDateTime.now(),
        progressInPercentage = 30,
        finishedToRead = null,
    )

    val profile = Profile(
        id = "kj98bsd",
        giveUp = listOf(),
        alreadyRead = listOf(),
        firstName = "Oliver",
        lastName = "Aceituna",
        following = listOf(),
        userShelves = listOf(),
        bookProgress = listOf(),
    )

    val comment = Comment(
        id = "dsoihnw98",
        text = "Esta buena esta parte",
//        user = profile,
        username = "Test",
        publishedDate = LocalDateTime.now(),
        hasSpoilers = false,
        parentCommentId = null,
        pageNumber = null,
        percentage = 33,
        votesCounter = 22,
    )

    val book = Book(
        authors = listOf("Comodoer"),
        title = "Laying Down Above",
        description = "",
        credits = listOf(),
        id = "w98hidn",
        isbn = listOf("", ""),
        picture = "",
        publishedDate = "",
        publisher = "",
        thumbnail = "",
        totalPages = 544,
        totalRating = 2.0,
        readingStatus = BookReadingStatus.READING,
        peopleReadingIt = 6,
        totalComments = 5,
        genres = listOf("", ""),
        allReviews = listOf(),
        userRating = null,
    )

    private val resultBookApi = Result(
        authors = listOf("Comodoer"),
        title = "Laying Down Above",
        description = "",
        credits = listOf(),
        id = "w98hidn",
        isbn = listOf("", ""),
        picture = "",
        publishedDate = "",
        publisher = "",
        totalPages = 544,
        thumbnail = "",
        genres = listOf("", ""),
        extras = Extras(
            totalRating = 2.0,
            readingStatus = 1,
            peopleReadingIt = 6,
            totalComments = 5,
            allReviews = listOf(),
            userRating = null,
        ),
    )

    val bookApi = BooksApi(
        result = listOf(resultBookApi)
    )

    val ratingBook = UserBookRating(
        id = "dkjbe98h",
        book = book,
        user = profile,
        rating = 5.5,
        review = "",
        date = LocalDateTime.now(),
    )

    val report = Report(
        id = "s98hioew",
        comment = comment,
        user = profile,
        reason = ReportReason.Spam,
    )
}
package com.ayoprez.sobuu.shared.features

import com.ayoprez.sobuu.shared.models.*
import java.time.LocalDateTime

object Utils {

    val bookProgress = BookProgress(
        id = "ewois98",
        percentage = null,
        page = 33,
        finished = false,
        giveUp = false
    )

    val profile = Profile(
        id = "kj98bsd",
        giveUp = listOf(),
        alreadyRead = listOf(),
        firstName = "Oliver",
        lastName = "Aceituna",
        following = listOf(),
        userShelves = listOf(),
    )

    val comment = Comment(
        id = "dsoihnw98",
        text = "Esta buena esta parte",
        user = profile,
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
        bookComments = listOf(comment),
        bookRating = listOf(),
        credits = listOf(),
        id = "w98hidn",
        isbn = listOf("", ""),
        picture = "",
        publishedDate = "",
        publisher = "",
        totalPages = 544
    )

    val ratingBook = UserBookRating(
        id = "dkjbe98h",
        book = book,
        user = profile,
        rating = 5.5,
        review = "",
    )

    val report = Report(
        id = "s98hioew",
        comment = comment,
        user = profile,
        reason = ReportReason.Spam,
    )
}
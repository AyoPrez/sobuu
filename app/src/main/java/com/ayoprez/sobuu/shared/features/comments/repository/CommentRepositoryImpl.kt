package com.ayoprez.sobuu.shared.features.comments.repository

import com.ayoprez.sobuu.shared.features.comments.database.ICommentLocalData
import com.ayoprez.sobuu.shared.features.comments.remote.CommentError
import com.ayoprez.sobuu.shared.features.comments.remote.CommentResult
import com.ayoprez.sobuu.shared.features.comments.remote.ICommentRemoteData
import com.ayoprez.sobuu.shared.models.bo_models.Comment
import com.ayoprez.sobuu.shared.models.bo_models.Report
import com.ayoprez.sobuu.shared.models.bo_models.ReportReason
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentRemoteData: ICommentRemoteData,
    private val commentLocalData: ICommentLocalData
): ICommentRepository {

    override suspend fun addComment(
        bookId: String,
        text: String?,
        page: Number?,
        percentage: Number?,
        parentCommentId: String?,
        hasSpoilers: Boolean
    ): CommentResult<Comment> = execute {
        commentRemoteData.addComment(
            sessionToken = it,
            bookId = bookId,
            text = text,
            page = page,
            percentage = percentage,
            parentCommentId = parentCommentId,
            hasSpoilers = hasSpoilers,
        )
    }

    override suspend fun removeComment(bookId: String, commentId: String): CommentResult<Unit> = execute {
        commentRemoteData.removeComment(
            sessionToken = it,
            bookId = bookId,
            commentId = commentId,
        )
    }

    override suspend fun editComment(
        bookId: String,
        commentId: String,
        text: String?,
        hasSpoilers: Boolean
    ): CommentResult<Comment> = execute {
        commentRemoteData.editComment(
            sessionToken = it,
            bookId = bookId,
            commentId = commentId,
            text = text,
            hasSpoilers = hasSpoilers,
        )
    }

    override suspend fun getCommentsInPageOrPercentage(
        bookId: String,
        page: Number?,
        percentage: Number?
    ): CommentResult<List<Comment>> = execute {
        commentRemoteData.getCommentsInPageOrPercentage(
            sessionToken = it,
            bookId = bookId,
            page = page,
            percentage = percentage,
        )
    }

    override suspend fun increaseCommentVoteCounter(commentId: String): CommentResult<Comment> =
        execute {
            commentRemoteData.increaseCommentVoteCounter(
                sessionToken = it,
                commentId = commentId,
            )
    }

    override suspend fun decreaseCommentVoteCounter(commentId: String): CommentResult<Comment> =
        execute {
        commentRemoteData.decreaseCommentVoteCounter(
            sessionToken = it,
            commentId = commentId,
        )
    }

    override suspend fun reportComment(
        commentId: String,
        reason: ReportReason
    ): CommentResult<Report> = execute {
        commentRemoteData.reportComment(
            sessionToken = it,
            commentId = commentId,
            reason = reason,
        )
    }

    private suspend fun <T>execute(func: suspend (sessionToken: String) -> CommentResult<T>) : CommentResult<T> {
        val sessionToken = commentLocalData.getSessionToken() ?: return CommentResult.Error(
            CommentError.InvalidSessionTokenError)
        return func(sessionToken)
    }
}
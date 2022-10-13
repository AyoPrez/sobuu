package com.ayoprez.sobuu.shared.features.comments.remote

import com.ayoprez.sobuu.shared.models.bo_models.Comment
import com.ayoprez.sobuu.shared.models.bo_models.Report
import com.ayoprez.sobuu.shared.models.bo_models.ReportReason
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class CommentRemoteDataImpl @Inject constructor(
    private val api: CommentApi
): ICommentRemoteData {

    override suspend fun addComment(
        sessionToken: String?,
        bookId: String,
        text: String?,
        page: Number?,
        percentage: Number?,
        parentCommentId: String?,
        hasSpoilers: Boolean
    ): CommentResult<Comment> {
        if (bookId.isBlank()) return CommentResult.Error(CommentError.InvalidBookIdError)
        if (text.isNullOrBlank()) return CommentResult.Error(CommentError.EmptyFieldError)
        if (page == null && percentage == null) return CommentResult.Error(CommentError.InvalidDoublePageAndPercentageNumberError)
        if (page != null && percentage != null) return CommentResult.Error(CommentError.InvalidDoublePageAndPercentageNumberError)
        if (page != null && page.toInt() < 0) return CommentResult.Error(CommentError.InvalidPageNumberError)
        if (percentage != null && (percentage.toByte() < 0 || percentage.toByte() > 100)) return CommentResult.Error(CommentError.InvalidPercentageNumberError)
        return execute(sessionToken) {
            api.addComment(
                sessionToken = it,
                bookId = bookId,
                page = page,
                percentage = percentage,
                text = text,
                parentCommentId = parentCommentId,
                hasSpoilers = hasSpoilers,
            )
        }
    }

    override suspend fun removeComment(
        sessionToken: String?,
        bookId: String,
        commentId: String
    ): CommentResult<Unit> {
        if (bookId.isBlank()) return CommentResult.Error(CommentError.InvalidBookIdError)
        if (commentId.isBlank()) return CommentResult.Error(CommentError.InvalidCommentIdError)
        return execute(sessionToken) {
            api.removeComment(
                sessionToken = it,
                bookId = bookId,
                commentId = commentId,
            )
        }
    }

    override suspend fun editComment(
        sessionToken: String?,
        bookId: String,
        commentId: String,
        text: String?,
        hasSpoilers: Boolean
    ): CommentResult<Comment> {
        if (bookId.isBlank()) return CommentResult.Error(CommentError.InvalidBookIdError)
        if (commentId.isBlank()) return CommentResult.Error(CommentError.InvalidCommentIdError)
        if (text.isNullOrBlank()) return CommentResult.Error(CommentError.EmptyFieldError)
        return execute(sessionToken) {
            api.editComment(
                sessionToken = it,
                bookId = bookId,
                commentId = commentId,
                text = text,
                hasSpoilers = hasSpoilers,
            )
        }
    }

    override suspend fun getCommentsInPageOrPercentage(
        sessionToken: String?,
        bookId: String,
        page: Number?,
        percentage: Number?
    ): CommentResult<List<Comment>> {
        if (bookId.isBlank()) return CommentResult.Error(CommentError.InvalidBookIdError)
        if (page == null && percentage == null) return CommentResult.Error(CommentError.InvalidDoublePageAndPercentageNumberError)
        if (page != null && percentage != null) return CommentResult.Error(CommentError.InvalidDoublePageAndPercentageNumberError)
        if (page != null && page.toInt() < 0) return CommentResult.Error(CommentError.InvalidPageNumberError)
        if (percentage != null && (percentage.toByte() < 0 || percentage.toByte() > 100)) return CommentResult.Error(CommentError.InvalidPercentageNumberError)
        return execute(sessionToken) {
            api.getCommentsFromPageOrPercentage(
                sessionToken = it,
                bookId = bookId,
                page = page,
                percentage = percentage,
            )
        }
    }

    override suspend fun increaseCommentVoteCounter(
        sessionToken: String?,
        commentId: String
    ): CommentResult<Comment> {
        if (commentId.isBlank()) return CommentResult.Error(CommentError.InvalidCommentIdError)
        return execute(sessionToken) {
            api.increaseVoteCounterComment(
                sessionToken = it,
                commentId = commentId,
            )
        }
    }

    override suspend fun decreaseCommentVoteCounter(
        sessionToken: String?,
        commentId: String
    ): CommentResult<Comment> {
        if (commentId.isBlank()) return CommentResult.Error(CommentError.InvalidCommentIdError)
        return execute(sessionToken) {
            api.decreaseVoteCounterComment(
                sessionToken = it,
                commentId = commentId,
            )
        }
    }

    override suspend fun reportComment(
        sessionToken: String?,
        commentId: String,
        reason: ReportReason
    ): CommentResult<Report> {
        if (commentId.isBlank()) return CommentResult.Error(CommentError.InvalidCommentIdError)
        return execute(sessionToken) {
            api.reportComment(
                sessionToken = it,
                commentId = commentId,
                reason = reason.ordinal.toByte()
            )
        }
    }

    private suspend fun <T>execute(sessionToken: String?, func: suspend (sessionToken: String) -> Response<T>): CommentResult<T> {
        return try {
            if (sessionToken.isNullOrBlank()) return CommentResult.Error(CommentError.InvalidSessionTokenError)

            val result = func(sessionToken)

            if (result.body() == null && result.errorBody() != null) return handleResponseError(
                result.errorBody()
            )

            return CommentResult.Success(data = result.body())
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> CommentResult.Error(CommentError.UnauthorizedQueryError)
                209 -> CommentResult.Error(CommentError.InvalidSessionTokenError)
                else -> CommentResult.Error(CommentError.UnknownError)
            }
        } catch (e: Exception) {
            CommentResult.Error(CommentError.UnknownError)
        }
    }

    private fun <T>handleResponseError(errorBody: ResponseBody?): CommentResult<T> {
        if (errorBody == null) return CommentResult.Error(error = CommentError.UnknownError)
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when(it) {
                    101 -> CommentResult.Error(CommentError.UnauthorizedQueryError)
                    141 -> CommentResult.Error(CommentError.ProcessingQueryError)
                    124 -> CommentResult.Error(CommentError.TimeOutError)
                    209 -> CommentResult.Error(CommentError.InvalidSessionTokenError)
                    else -> CommentResult.Error(CommentError.UnknownError)
                }
            }
    }
}
package com.ayoprez.sobuu.shared.features.comments.repository

import com.ayoprez.sobuu.shared.features.comments.remote.CommentResult
import com.ayoprez.sobuu.shared.models.Comment
import com.ayoprez.sobuu.shared.models.Report
import com.ayoprez.sobuu.shared.models.ReportReason

interface ICommentRepository {

    suspend fun addComment(bookId: String, text: String?, page: Number?,
                           percentage: Number?, parentCommentId: String?,
                           hasSpoilers: Boolean) : CommentResult<Comment>

    suspend fun removeComment(bookId: String, commentId: String) : CommentResult<Unit>

    suspend fun editComment(bookId: String, commentId: String, text: String?,
                            hasSpoilers: Boolean) : CommentResult<Comment>

    suspend fun getCommentsInPageOrPercentage(bookId: String,
                                              page: Number?,
                                              percentage: Number?) : CommentResult<List<Comment>>

    suspend fun increaseCommentVoteCounter(commentId: String) : CommentResult<Comment>

    suspend fun decreaseCommentVoteCounter(commentId: String) : CommentResult<Comment>

    suspend fun reportComment(commentId: String, reason: ReportReason) : CommentResult<Report>
}
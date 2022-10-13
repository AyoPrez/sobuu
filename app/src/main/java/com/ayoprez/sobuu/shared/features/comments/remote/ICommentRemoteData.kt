package com.ayoprez.sobuu.shared.features.comments.remote

import com.ayoprez.sobuu.shared.models.bo_models.Comment
import com.ayoprez.sobuu.shared.models.bo_models.Report
import com.ayoprez.sobuu.shared.models.bo_models.ReportReason

interface ICommentRemoteData {

    suspend fun addComment(sessionToken: String?, bookId: String,
                           text: String?, page: Number?,
                           percentage: Number?, parentCommentId: String?,
                           hasSpoilers: Boolean) : CommentResult<Comment>

    suspend fun removeComment(sessionToken: String?, bookId: String,
                           commentId: String) : CommentResult<Unit>

    suspend fun editComment(sessionToken: String?, bookId: String,
                            commentId: String, text: String?,
                            hasSpoilers: Boolean) : CommentResult<Comment>

    suspend fun getCommentsInPageOrPercentage(sessionToken: String?, bookId: String,
                                              page: Number?,
                                              percentage: Number?) : CommentResult<List<Comment>>

    suspend fun increaseCommentVoteCounter(sessionToken: String?, commentId: String) : CommentResult<Comment>

    suspend fun decreaseCommentVoteCounter(sessionToken: String?, commentId: String) : CommentResult<Comment>

    suspend fun reportComment(sessionToken: String?, commentId: String, reason: ReportReason) : CommentResult<Report>
}
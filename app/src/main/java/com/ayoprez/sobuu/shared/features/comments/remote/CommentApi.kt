package com.ayoprez.sobuu.shared.features.comments.remote

import com.ayoprez.sobuu.shared.models.Comment
import com.ayoprez.sobuu.shared.models.Report
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CommentApi {

    @FormUrlEncoded
    @POST("functions/addComment")
    suspend fun addComment(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("text") text: String,
        @Field("page") page: Number?,
        @Field("percentage") percentage: Number?,
        @Field("parentCommentId") parentCommentId: String?,
        @Field("hasSpoilers") hasSpoilers: Boolean,
    ): Response<Comment>

    @FormUrlEncoded
    @POST("functions/removeComment")
    suspend fun removeComment(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("commentId") commentId: String,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("functions/editComment")
    suspend fun editComment(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("commentId") commentId: String,
        @Field("text") text: String,
        @Field("hasSpoilers") hasSpoilers: Boolean,
    ): Response<Comment>

    @FormUrlEncoded
    @POST("functions/getCommentsFromPageOrPercentage")
    suspend fun getCommentsFromPageOrPercentage(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("page") page: Number?,
        @Field("percentage") percentage: Number?,
    ): Response<List<Comment>>

    @FormUrlEncoded
    @POST("functions/increaseVoteCounterComment")
    suspend fun increaseVoteCounterComment(
        @Field("sessionToken") sessionToken: String,
        @Field("commentId") commentId: String,
    ): Response<Comment>

    @FormUrlEncoded
    @POST("functions/decreaseVoteCounterComment")
    suspend fun decreaseVoteCounterComment(
        @Field("sessionToken") sessionToken: String,
        @Field("commentId") commentId: String,
    ): Response<Comment>

    @FormUrlEncoded
    @POST("functions/reportComment")
    suspend fun reportComment(
        @Field("sessionToken") sessionToken: String,
        @Field("commentId") commentId: String,
        @Field("reportReason") reason: Byte,
    ): Response<Report>
}
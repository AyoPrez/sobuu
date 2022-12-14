package com.ayoprez.sobuu.shared.features.book.remote

import com.ayoprez.sobuu.shared.models.api_models.books_api.BooksApi
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.CurrentlyReadingBookApi
import com.ayoprez.sobuu.shared.models.api_models.currently_reading_api.UpdateProgressResultApi
import com.ayoprez.sobuu.shared.models.api_models.finished_books_api.FinishedBooksApi
import com.ayoprez.sobuu.shared.models.api_models.give_up_books_api.GiveUpBooksApi
import com.ayoprez.sobuu.shared.models.bo_models.Book
import com.ayoprez.sobuu.shared.models.bo_models.BookProgress
import com.ayoprez.sobuu.shared.models.bo_models.Comment
import com.ayoprez.sobuu.shared.models.bo_models.UserBookRating
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BookApi {

    @FormUrlEncoded
    @POST("functions/getUserCurrentReadingBook")
    suspend fun getUserCurrentReadingBook(
        @Field("sessionToken") sessionToken: String,
    ): Response<CurrentlyReadingBookApi>

    @FormUrlEncoded
    @POST("functions/getUserAlreadyReadBooks")
    suspend fun getUserAlreadyReadBooks(
        @Field("sessionToken") sessionToken: String,
    ): Response<FinishedBooksApi>

    @FormUrlEncoded
    @POST("functions/getUserGiveUpBooks")
    suspend fun getUserGiveUpBooks(
        @Field("sessionToken") sessionToken: String,
    ): Response<GiveUpBooksApi>

    @FormUrlEncoded
    @POST("functions/search")
    suspend fun searchBook(
        @Field("sessionToken") sessionToken: String,
        @Field("term") term: String,
        @Field("lang") lang: String,
        @Field("searchFurther") searchFurther: Boolean,
    ): Response<BooksApi>

    @FormUrlEncoded
    @POST("functions/getAllCommentsFromBookAtPage")
    suspend fun getCommentsFromPage(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("pageNum") pageNum: Number,
    ): Response<List<Comment>>

    @FormUrlEncoded
    @POST("functions/getAllCommentsFromBookAtPercentage")
    suspend fun getCommentsFromPercentage(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("percentage") percentage: Number,
    ): Response<List<Comment>>

    @FormUrlEncoded
    @POST("functions/setBookAsCurrentlyReading")
    suspend fun setBookAsCurrentlyReading(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("functions/updateBookProgress")
    suspend fun updateProgressBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("percentage") percentage: Number?,
        @Field("page") page: Number?,
        @Field("finished") finished: Boolean,
        @Field("giveUp") giveUp: Boolean,
    ): Response<UpdateProgressResultApi>

    @FormUrlEncoded
    @POST("functions/getBookProgress")
    suspend fun getBookProgress(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<UpdateProgressResultApi>

    @FormUrlEncoded
    @POST("functions/addRating")
    suspend fun ratingBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
        @Field("rating") rating: Double,
        @Field("reviewText") reviewText: String,
    ): Response<Book>

    @FormUrlEncoded
    @POST("functions/getUserRatingInBook")
    suspend fun getUserRatingInBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<UserBookRating>

    @FormUrlEncoded
    @POST("functions/getRatingListFromBook")
    suspend fun getRatingListFromBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<List<UserBookRating>>

    @FormUrlEncoded
    @POST("functions/removeRating")
    suspend fun removeRating(
        @Field("sessionToken") sessionToken: String,
        @Field("ratingId") ratingId: String,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("functions/finishBook")
    suspend fun finishBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<BookProgress>

    @FormUrlEncoded
    @POST("functions/giveUpWithBook")
    suspend fun giveUpWithBook(
        @Field("sessionToken") sessionToken: String,
        @Field("bookId") bookId: String,
    ): Response<BookProgress>
}
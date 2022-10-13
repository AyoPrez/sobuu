package com.ayoprez.sobuu.shared.features.shelf.remote

import com.ayoprez.sobuu.shared.models.bo_models.Shelf
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ShelfApi {

    @FormUrlEncoded
    @POST("functions/searchShelf")
    suspend fun searchShelf(
        @Field("sessionToken") sessionToken: String,
        @Field("term") term: String
    ): Response<List<Shelf>>

    @FormUrlEncoded
    @POST("functions/getAllShelfsFromUser")
    suspend fun getAllUserShelves(
        @Field("sessionToken") sessionToken: String
    ): Response<List<Shelf>>

    @FormUrlEncoded
    @POST("functions/createShelf")
    suspend fun createShelf(
        @Field("sessionToken") sessionToken: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("isPublic") isPublic: Boolean
    ): Response<Shelf>

    @FormUrlEncoded
    @POST("functions/changeShelfName")
    suspend fun changeShelfName(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
        @Field("newName") newName: String,
    ): Response<Shelf>

    @FormUrlEncoded
    @POST("functions/changeShelfDescription")
    suspend fun changeShelfDescription(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
        @Field("newDescription") newDescription: String,
    ): Response<Shelf>

    @FormUrlEncoded
    @POST("functions/changeShelfPrivacy")
    suspend fun changeShelfPrivacy(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
        @Field("isPublic") isPublic: Boolean,
    ): Response<Shelf>

    @FormUrlEncoded
    @POST("functions/addBookToShelf")
    suspend fun addBookToShelf(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
        @Field("bookId") bookId: String,
    ): Response<Shelf>

    @FormUrlEncoded
    @POST("functions/removeBookFromShelf")
    suspend fun removeBookFromShelf(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
        @Field("bookId") bookId: String,
    ): Response<Shelf>
    
    @FormUrlEncoded
    @POST("functions/removeShelf")
    suspend fun removeShelf(
        @Field("sessionToken") sessionToken: String,
        @Field("shelfId") shelfId: String,
    ): Response<Unit>
}
package com.ayoprez.sobuu.shared.features.shelf.remote

import com.ayoprez.sobuu.shared.models.Shelf

interface IShelfRemoteData {
    suspend fun searchShelvesFromApi(sessionToken: String?, term: String): ShelfResult<List<Shelf>>

    suspend fun getAllUserShelves(sessionToken: String?): ShelfResult<List<Shelf>>

    suspend fun createShelf(sessionToken: String?, name: String, description: String, isPublic: Boolean): ShelfResult<Shelf>

    suspend fun changeShelfName(sessionToken: String?, shelfId: String, newName: String): ShelfResult<Shelf>

    suspend fun changeShelfDescription(sessionToken: String?, shelfId: String, newDescription: String): ShelfResult<Shelf>

    suspend fun changeShelfPrivacy(sessionToken: String?, shelfId: String, isPublic: Boolean): ShelfResult<Shelf>

    suspend fun addBookToShelf(sessionToken: String?, shelfId: String, bookId: String): ShelfResult<Shelf>
}
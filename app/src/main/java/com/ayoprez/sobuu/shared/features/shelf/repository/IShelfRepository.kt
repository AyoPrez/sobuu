package com.ayoprez.sobuu.shared.features.shelf.repository

import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfResult
import com.ayoprez.sobuu.shared.models.Shelf

interface IShelfRepository {

    suspend fun createShelf(name: String,
                            description: String,
                            isPublic: Boolean): ShelfResult<Shelf>

    suspend fun searchShelf(term: String): ShelfResult<List<Shelf>>

    suspend fun getAllUserShelves(): ShelfResult<List<Shelf>>

    suspend fun changeShelfName(shelfId: String, newName: String): ShelfResult<Shelf>

    suspend fun changeShelfDescription(shelfId: String, newDescription: String): ShelfResult<Shelf>

    suspend fun changeShelfPrivacy(shelfId: String, isPublic: Boolean): ShelfResult<Shelf>

    suspend fun addBookToShelf(shelfId: String, bookId: String): ShelfResult<Shelf>

    suspend fun removeBookFromShelf(shelfId: String, bookId: String): ShelfResult<Shelf>

    suspend fun removeShelf(shelfId: String): ShelfResult<Unit>
}
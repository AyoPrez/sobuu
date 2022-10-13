package com.ayoprez.sobuu.shared.features.shelf.repository

import com.ayoprez.sobuu.shared.features.shelf.database.IShelfLocalData
import com.ayoprez.sobuu.shared.features.shelf.remote.IShelfRemoteData
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfError
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfResult
import com.ayoprez.sobuu.shared.models.bo_models.Shelf
import javax.inject.Inject

class ShelfRepositoryImpl @Inject constructor(
    private val shelfRemoteData: IShelfRemoteData,
    private val shelfLocalData: IShelfLocalData
): IShelfRepository {

    override suspend fun createShelf(
        name: String,
        description: String,
        isPublic: Boolean
    ): ShelfResult<Shelf>  = execute { shelfRemoteData.createShelf(
            sessionToken = it,
            name = name,
            description = description,
            isPublic = isPublic
        )
    }

    override suspend fun getAllUserShelves(): ShelfResult<List<Shelf>> = execute {
            shelfRemoteData.getAllUserShelves(sessionToken = it)
        }

    override suspend fun searchShelf(term: String): ShelfResult<List<Shelf>> = execute {
        shelfRemoteData.searchShelvesFromApi(
            sessionToken = it,
            term = term,
        )
    }

    override suspend fun changeShelfName(shelfId: String, newName: String): ShelfResult<Shelf> = execute {
        shelfRemoteData.changeShelfName(
            sessionToken = it,
            shelfId = shelfId,
            newName = newName,
        )
    }

    override suspend fun changeShelfDescription(
        shelfId: String,
        newDescription: String
    ): ShelfResult<Shelf> = execute {
        shelfRemoteData.changeShelfDescription(
            sessionToken = it,
            shelfId = shelfId,
            newDescription = newDescription,
        )
    }

    override suspend fun changeShelfPrivacy(
        shelfId: String,
        isPublic: Boolean
    ): ShelfResult<Shelf> = execute {
        shelfRemoteData.changeShelfPrivacy(
            sessionToken = it,
            shelfId = shelfId,
            isPublic = isPublic,
        )
    }

    override suspend fun addBookToShelf(shelfId: String, bookId: String): ShelfResult<Shelf> = execute {
        shelfRemoteData.addBookToShelf(
            sessionToken = it,
            shelfId = shelfId,
            bookId = bookId,
        )
    }

    override suspend fun removeBookFromShelf(shelfId: String, bookId: String): ShelfResult<Shelf> = execute {
        shelfRemoteData.removeBookFromShelf(
            sessionToken = it,
            shelfId = shelfId,
            bookId = bookId,
        )
    }

    override suspend fun removeShelf(shelfId: String): ShelfResult<Unit> = execute {
        shelfRemoteData.removeShelf(
            sessionToken = it,
            shelfId = shelfId,
        )
    }

    private suspend fun <T>execute(func: suspend (sessionToken: String) -> ShelfResult<T>) : ShelfResult<T> {
        val sessionToken = shelfLocalData.getSessionToken() ?: return ShelfResult.Error(ShelfError.InvalidSessionTokenError)
        return func(sessionToken)
    }

}
package com.ayoprez.sobuu.shared.features.shelf.remote

import com.ayoprez.sobuu.shared.models.Shelf

interface IShelfRemoteData {
    suspend fun searchShelvesFromApi(sessionToken: String?, term: String): ShelfResult<List<Shelf>>

    suspend fun getAllUserShelves(sessionToken: String?): ShelfResult<List<Shelf>>
}
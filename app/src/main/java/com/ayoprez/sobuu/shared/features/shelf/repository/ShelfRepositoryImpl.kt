package com.ayoprez.sobuu.shared.features.shelf.repository

import com.ayoprez.sobuu.shared.features.shelf.database.IShelfLocalData
import com.ayoprez.sobuu.shared.features.shelf.remote.IShelfRemoteData
import javax.inject.Inject

class ShelfRepositoryImpl @Inject constructor(
    private val shelfRemoteData: IShelfRemoteData,
    private val shelfLocalData: IShelfLocalData
): IShelfRepository {
}
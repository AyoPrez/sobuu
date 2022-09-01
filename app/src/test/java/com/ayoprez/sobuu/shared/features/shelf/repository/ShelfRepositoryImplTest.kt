package com.ayoprez.sobuu.shared.features.shelf.repository

import com.ayoprez.sobuu.shared.features.shelf.database.IShelfLocalData
import com.ayoprez.sobuu.shared.features.shelf.remote.IShelfRemoteData
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfError
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test


internal class ShelfRepositoryImplTest {

    private lateinit var shelfRepo: IShelfRepository

    @MockK
    lateinit var remoteData: IShelfRemoteData

    @MockK
    lateinit var localData: IShelfLocalData

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        shelfRepo = ShelfRepositoryImpl(shelfRemoteData = remoteData, shelfLocalData = localData)
    }

    // region Create
    @Test
    fun `Create Shelf - Everything good`() {
        coEvery { remoteData.createShelf(any(), any(), any(), any()) } returns ShelfResult.Success()
        coEvery { localData.getSessionToken() } returns "TestToken"

        val result = runBlocking {
            shelfRepo.createShelf("test", "test", true)
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Success::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Create Shelf - Empty session token, should return error`() {
        coEvery { remoteData.createShelf(any(), any(), any(), any()) } returns ShelfResult.Error(error = ShelfError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            shelfRepo.createShelf("test", "test", true)
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Create Shelf - Null session token, should return error`() {
        coEvery { remoteData.createShelf(any(), any(), any(), any()) } returns ShelfResult.Error(error = ShelfError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            shelfRepo.createShelf("test", "test", true)
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }
    //endregion

    //region Search shelf

    @Test
    fun `Search Shelf - Everything good`() {
        coEvery { remoteData.searchShelvesFromApi(any(), any()) } returns ShelfResult.Success()
        coEvery { localData.getSessionToken() } returns "TestToken"

        val result = runBlocking {
            shelfRepo.searchShelf("test")
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Success::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Search Shelf - Empty session token, should return error`() {
        coEvery { remoteData.searchShelvesFromApi(any(), any()) } returns ShelfResult.Error(error = ShelfError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            shelfRepo.searchShelf("test")
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Search Shelf - Null session token, should return error`() {
        coEvery { remoteData.searchShelvesFromApi(any(), any()) } returns ShelfResult.Error(error = ShelfError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            shelfRepo.searchShelf("test")
        }

        MatcherAssert.assertThat(
            result, instanceOf(ShelfResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }
    //endregion

    //region Get all shelves from user

    //endregion

    //region Change name

    //endregion

    //region Change description

    //endregion

    //region Change privacy

    //endregion

    //region Add book to shelf

    //endregion

    //region Remove book from shelf

    //endregion

    //region Remove shelf

    //endregion
}
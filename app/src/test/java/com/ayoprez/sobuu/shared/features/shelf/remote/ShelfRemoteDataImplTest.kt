package com.ayoprez.sobuu.shared.features.shelf.remote

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

internal class ShelfRemoteDataImplTest {

    private lateinit var shelfRemote: IShelfRemoteData

    @MockK
    lateinit var shelfApi: ShelfApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        shelfRemote = ShelfRemoteDataImpl(shelfApi)
    }

    //region Search
    @Test
    fun `Search - Not null`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("9hosnd9", "name")
        }

        assertEquals(result.data?.size, 0)
    }

    @Test
    fun `Search - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("", "name")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Search - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi(null, "name")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Search - If term is empty, should return empty term error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("sp8hsh908", "")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyTerm::class.java))
    }

    @Test
    fun `Search - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("sp8hsh908", "Fanta")
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Search - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("sp8hsh908", "Fanta")
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Search - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.searchShelf(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.searchShelvesFromApi("sp8hsh908", "Fanta")
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Get user shelves
    @Test
    fun `Get Shelves - Not null`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.getAllUserShelves("9hosnd9")
        }

        assertEquals(result.data?.size, 0)
    }

    @Test
    fun `Get Shelves - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.getAllUserShelves("")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Get Shelves - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.success(listOf())

        val result = runBlocking {
            shelfRemote.getAllUserShelves(null)
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Get Shelves - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.getAllUserShelves("sp8hsh908")
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Get Shelves - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.getAllUserShelves("sp8hsh908")
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Get Shelves - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.getAllUserShelves(any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.getAllUserShelves("s89hidks0")
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Create

    //endregion

    //region Change name

    //endregion

    //region Chane privacy

    //endregion

    //region Add book

    //endregion

    //region Remove book

    //endregion

    //region Remove

    //endregion
}
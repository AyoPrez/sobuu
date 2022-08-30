package com.ayoprez.sobuu.shared.features.shelf.remote

import com.ayoprez.sobuu.shared.models.Book
import com.ayoprez.sobuu.shared.models.Shelf
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
    private val book = Book(
        authors = listOf("Comodoer"),
        title = "Laying Down Above",
        description = "",
        bookComments = listOf(),
        bookRating = listOf(),
        credits = listOf(),
        id = "w98hidn",
        isbn = listOf("", ""),
        picture = "",
        publishedDate = "",
        publisher = "",
        totalPages = 544
    )

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
    @Test
    fun `Create Shelf - Not null`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "Fantasy", "Huge Fantasy", true)
        }

        assertEquals(result.data?.name, "Fantasy")
    }

    @Test
    fun `Create Shelf - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.createShelf("", "Fantasy", "Huge Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Create Shelf - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.createShelf(null, "Fantasy", "Huge Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Create Shelf - If shelf name is empty, should return empty name error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "", "Huge Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyName::class.java))
    }

    @Test
    fun `Create Shelf - If shelf description is empty, should return empty description error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "Fanta", "", true)
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyDescription::class.java))
    }

    @Test
    fun `Create Shelf - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "Fanta", "High Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Create Shelf - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "Fanta", "High Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Create Shelf - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.createShelf(any(), any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.createShelf("9hosnd9", "Fanta", "High Fantasy", true)
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Change name
    @Test
    fun `Change Name - Not null`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "aw98hiw98", "Huge Fantasy")
        }

        assertEquals(result.data?.name, "Fantasy")
    }

    @Test
    fun `Change Name - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfName("", "aw98hiw98", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Name - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfName(null, "aw98hiw98", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Name - If shelf shelf Id is empty, should return empty shelf id error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyShelfId::class.java))
    }

    @Test
    fun `Change Name - If shelf new name is empty, should return empty name error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "aw98hiw98", "")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyName::class.java))
    }

    @Test
    fun `Change Name - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Change Name - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Change Name - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.changeShelfName(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfName("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Change description
    @Test
    fun `Change Description - Not null`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Huge Fantasy", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "aw98hiw98", "Huge Fantasy")
        }

        assertEquals(result.data?.description, "Huge Fantasy")
    }

    @Test
    fun `Change Description - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfDescription("", "aw98hiw98", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Description - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfDescription(null, "aw98hiw98", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Description - If shelf shelf Id is empty, should return empty shelf id error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "", "Huge Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyShelfId::class.java))
    }

    @Test
    fun `Change Description - If shelf new description is empty, should return empty name error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "aw98hiw98", "")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyDescription::class.java))
    }

    @Test
    fun `Change Description - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Change Description - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Change Description - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.changeShelfDescription(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfDescription("9hosnd9", "aw98hiw98", "High Fantasy")
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Change privacy
    @Test
    fun `Change Privacy - Not null`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("9hosnd9", "aw98hiw98", true)
        }

        assertEquals(result.data?.name, "Fantasy")
    }

    @Test
    fun `Change Privacy - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("", "aw98hiw98", true)
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Privacy - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy(null, "aw98hiw98", true)
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Change Privacy - If shelf shelf Id is empty, should return empty shelf id error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("9hosnd9", "", true)
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyShelfId::class.java))
    }

    @Test
    fun `Change Privacy - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("9hosnd9", "aw98hiw98", true)
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Change Privacy - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("9hosnd9", "aw98hiw98", true)
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Change Privacy - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.changeShelfPrivacy(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.changeShelfPrivacy("9hosnd9", "aw98hiw98", true)
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Add book
    @Test
    fun `Add Book - Not null`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(book), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "aw98hiw98", "w98hidn")
        }

        assertEquals(result.data?.books?.size, 1)
    }

    @Test
    fun `Add Book - If session token is empty, should return invalid session token error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(book), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.addBookToShelf("", "aw98hiw98", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Add Book - If session token is null, should return invalid session token error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(book), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.addBookToShelf(null, "aw98hiw98", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.InvalidSessionTokenError::class.java))
    }

    @Test
    fun `Add Book - If shelf Id is empty, should return empty shelf id error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyShelfId::class.java))
    }

    @Test
    fun `Add Book - If book Id is empty, should return empty shelf id error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.success(Shelf(id="s98hwe", name="Fantasy", books = listOf(), description = "Fantasy for everybody", isPublic = true))

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "w09jdsli", "")
        }

        assertThat(result.error, instanceOf(ShelfError.EmptyBookId::class.java))
    }

    @Test
    fun `Add Book - If request return 141 error, should return processing query error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "aw98hiw98", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.ProcessingQueryError::class.java))
    }

    @Test
    fun `Add Book - If request return 124 error, should return time out error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "aw98hiw98", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.TimeOutError::class.java))
    }

    @Test
    fun `Add Book - If request return 101 error, should return unauthorized query error`() {
        coEvery { shelfApi.addBookToShelf(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            shelfRemote.addBookToShelf("9hosnd9", "aw98hiw98", "w98hidn")
        }

        assertThat(result.error, instanceOf(ShelfError.UnauthorizedQueryError::class.java))
    }
    //endregion

    //region Remove book

    //endregion

    //region Remove shelf

    //endregion
}
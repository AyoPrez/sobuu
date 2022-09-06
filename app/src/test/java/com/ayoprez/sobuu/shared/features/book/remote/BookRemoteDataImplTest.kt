package com.ayoprez.sobuu.shared.features.book.remote

import com.ayoprez.sobuu.shared.models.Book
import com.ayoprez.sobuu.shared.models.BookProgress
import com.ayoprez.sobuu.shared.models.Profile
import com.ayoprez.sobuu.shared.models.UserBookRating
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class BookRemoteDataImplTest {

    private lateinit var bookRemote: IBookRemoteData
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

    private val bookProgress = BookProgress(
        id = "ewois98",
        percentage = null,
        page = 33,
        finished = false,
        giveUp = false
    )

    private val profile = Profile(
        id = "kj98bsd",
        giveUp = listOf(),
        alreadyRead = listOf(),
        firstName = "Oliver",
        lastName = "Aceituna",
        following = listOf(),
        userShelves = listOf(),
    )

    private val ratingBook = UserBookRating(
        id = "dkjbe98h",
        book = book,
        user = profile,
        rating = 5.5,
        review = "",
    )

    @MockK
    lateinit var bookApi: BookApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        bookRemote = BookRemoteDataImpl(bookApi)
    }

    //region Get user currently reading
    @Test
    fun `Get user currently reading - Everything good`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook("9hosnd9")
        }

        assertEquals(result.data?.get(0)?.id, "w98hidn")
    }

    @Test
    fun `Get user currently reading - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook("")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get user currently reading - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook(null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get user currently reading - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get user currently reading - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get user currently reading - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getUserCurrentReadingBook(any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserCurrentReadingBook("sp8hsh908")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Search book
    @Test
    fun `Search book - Everything good`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.searchBook("9hosnd9", "Dorian")
        }

        assertEquals(result.data?.get(0)?.id, "w98hidn")
    }

    @Test
    fun `Search book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.searchBook("", "Dorian")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Search book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.searchBook(null, "Dorian")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Search book - If term is empty, should return empty search term error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.success(listOf(book))

        val result = runBlocking {
            bookRemote.searchBook("suierds9", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.EmptySearchTermError::class.java)
        )
    }

    @Test
    fun `Search book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.searchBook("sp8hsh908", "Dorian")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Search book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.searchBook("sp8hsh908", "Dorian")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Search book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.searchBook(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.searchBook("sp8hsh908", "Dorian")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get comments from page
    @Test
    fun `Get comments from page - Everything good`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("9hosnd9", "0iejsn", 44)
        }

        assertEquals(result.data?.size, 0)
    }

    @Test
    fun `Get comments from page - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("", "pioweo8", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPage(null, "weoinw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("suierds9", "", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If page number is negative, should return invalid page number error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("suierds9", "ew98hunw8", -43)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("sp8hsh908", "osihnw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("sp8hsh908", "w98inw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get comments from page - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getCommentsFromPage(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPage("sp8hsh908", "wep8oinw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get comments from percentage
    @Test
    fun `Get comments from percentage - Everything good`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("9hosnd9", "0iejsn", 44)
        }

        assertEquals(result.data?.size, 0)
    }

    @Test
    fun `Get comments from percentage - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("", "pioweo8", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage(null, "weoinw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("suierds9", "", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If percentage number is negative, should return invalid percentage number error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("suierds9", "ew98hunw8", -43)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If percentage number is bigger than 100, should return invalid percentage number error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.success(listOf())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("suierds9", "ew98hunw8", 134)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("sp8hsh908", "osihnw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("sp8hsh908", "w98inw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get comments from percentage - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getCommentsFromPercentage(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getCommentsFromPercentage("sp8hsh908", "wep8oinw98", 44)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Set book currently reading
    @Test
    fun `Set book currently reading - Everything good`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("9hosnd9", "0iejsn")
        }

        assertNotNull(result)
    }

    @Test
    fun `Set book currently reading - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("", "pioweo8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Set book currently reading - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading(null, "weoinw98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Set book currently reading - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("suierds9", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Set book currently reading - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("sp8hsh908", "osihnw98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Set book currently reading - If request return 124 error, should return time out error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("sp8hsh908", "w98inw98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Set book currently reading - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.setBookAsCurrentlyReading(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.setBookCurrentlyReading("sp8hsh908", "wep8oinw98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Update book process
    @Test
    fun `Update book process - Everything good`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("9hosnd9", "0iejsn", null, 9,
                finished = false,
                giveUp = false
            )
        }

        assertNotNull(result)
    }

    @Test
    fun `Update book process - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("", "pioweo8", null, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Update book process - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress(null, "weoinw98", null, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Update book process - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "", null, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Update book process - If percentage is minor of 0, should return invalid percentage error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "soinsjs98", -43, null,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Update book process - If percentage is bigger than 100, should return invalid percentage error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "wsoiw9s8", 104, null,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Update book process - If page number is minor than 0, should return invalid page number error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "osn90s8hs", null, -9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPageNumberError::class.java)
        )
    }

    @Test
    fun `Update book process - If percentage and page are null, should return invalid page number error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "dsmn98sn", null, null,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidPageNumberError::class.java)
        )
    }

    @Test
    fun `Update book process - If page and number has values, should return invalid double value error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "soined98", 10, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidDoubleValueError::class.java)
        )
    }

    @Test
    fun `Update book process - If finished and give up are true, should return invalid finished and giveup book value error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.updateBookProgress("suierds9", "wdskns98hs", null, 9,
                finished = true,
                giveUp = true
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidFinishedAndGiveUpBookValuesError::class.java)
        )
    }

    @Test
    fun `Update book process - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.updateBookProgress("sp8hsh908", "osihnw98", null, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Update book process - If request return 124 error, should return time out error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.updateBookProgress("sp8hsh908", "w98inw98", null, 9,
                finished = false,
                giveUp = false,
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Update book process - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.updateProgressBook(any(), any(), any(), any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.updateBookProgress("sp8hsh908", "wep8oinw98", null, 9,
                finished = false,
                giveUp = false
            )
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get Book progress
    @Test
    fun `Get book progress - Everything good`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.getBookProgress("9hosnd9", "ew9dinsd98")
        }

        assertEquals(result.data?.id, "ewois98")
    }

    @Test
    fun `Get book progress - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.getBookProgress("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get book progress - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.getBookProgress(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get book progress - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.getBookProgress("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get book progress - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getBookProgress("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get book progress - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getBookProgress("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get book progress - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getBookProgress(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getBookProgress("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Rating book
    @Test
    fun `Rating book - Everything good`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("9hosnd9", "0iejsn", 7.0, "")
        }

        assertNotNull(result)
    }

    @Test
    fun `Rating book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("", "pioweo8", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Rating book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook(null, "weoinw98", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Rating book - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("suierds9", "", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Rating book - If rate is minor than 0, should return invalid rate error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("suierds9", "sñlmo9sdn", -7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidRateNumberError::class.java)
        )
    }

    @Test
    fun `Rating book - If rate is higher than 10, should return invalid rate error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("suierds9", "sñmosih", 17.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidRateNumberError::class.java)
        )
    }

    @Test
    fun `Rating book - If rate is a number with decimals, should run fine`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.success(book)

        val result = runBlocking {
            bookRemote.rateBook("suierds9", "dslnos8", 7.5, "")
        }

        assertEquals(result.data?.id, "w98hidn")
    }

    @Test
    fun `Rating book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.rateBook("sp8hsh908", "osihnw98", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Rating book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.rateBook("sp8hsh908", "w98inw98", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Rating book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.ratingBook(any(), any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.rateBook("sp8hsh908", "wep8oinw98", 7.0, "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get user Rating In Book
    @Test
    fun `Get user Rating In Book - Everything good`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.success(ratingBook)

        val result = runBlocking {
            bookRemote.getUserRatingInBook("9hosnd9", "ew9dinsd98")
        }

        assertEquals(result.data?.rating, 5.5)
    }

    @Test
    fun `Get user Rating In Book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.success(ratingBook)

        val result = runBlocking {
            bookRemote.getUserRatingInBook("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get user Rating In Book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.success(ratingBook)

        val result = runBlocking {
            bookRemote.getUserRatingInBook(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get user Rating In Book - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.success(ratingBook)

        val result = runBlocking {
            bookRemote.getUserRatingInBook("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get user Rating In Book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserRatingInBook("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get user Rating In Book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserRatingInBook("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get user Rating In Book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getUserRatingInBook(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getUserRatingInBook("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get Rating list from Book
    @Test
    fun `Get Rating list from Book - Everything good`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.success(listOf(ratingBook))

        val result = runBlocking {
            bookRemote.getRatingListFromBook("9hosnd9", "ew9dinsd98")
        }

        assertEquals(result.data?.get(0)?.rating, 5.5)
    }

    @Test
    fun `Get Rating list from Book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.success(listOf(ratingBook))

        val result = runBlocking {
            bookRemote.getRatingListFromBook("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get Rating list from Book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.success(listOf(ratingBook))

        val result = runBlocking {
            bookRemote.getRatingListFromBook(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get Rating list from Book - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.success(listOf(ratingBook))

        val result = runBlocking {
            bookRemote.getRatingListFromBook("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get Rating list from Book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getRatingListFromBook("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `GGet Rating list from Book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getRatingListFromBook("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get Rating list from Book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.getRatingListFromBook(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.getRatingListFromBook("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Remove rating
    @Test
    fun `Remove rating - Everything good`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.removeRating("9hosnd9", "ew9dinsd98")
        }

        assertNotNull(result)
    }

    @Test
    fun `Remove rating - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.removeRating("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Remove rating - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.removeRating(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Remove rating - If rate id is empty, should return invalid rate id error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            bookRemote.removeRating("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidRateIdError::class.java)
        )
    }

    @Test
    fun `Remove rating - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.removeRating("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Remove rating - If request return 124 error, should return time out error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.removeRating("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Remove rating - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.removeRating(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.removeRating("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Finish book
    @Test
    fun `Finish book - Everything good`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.finishBook("9hosnd9", "ew9dinsd98")
        }

        assertEquals(result.data?.id, "ewois98")
    }

    @Test
    fun `Finish book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.finishBook("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Finish book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.finishBook(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Finish book - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.finishBook("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Finish book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.finishBook("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Finish book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.finishBook("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Finish book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.finishBook(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.finishBook("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Give up with book
    @Test
    fun `Give up with book - Everything good`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.giveUpWithBook("9hosnd9", "ew9dinsd98")
        }

        assertEquals(result.data?.id, "ewois98")
    }

    @Test
    fun `Give up with book - If session token is empty, should return invalid session token error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.giveUpWithBook("", "dsoiksnz98")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Give up with book - If session token is null, should return invalid session token error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.giveUpWithBook(null, "slkns98h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Give up with book - If book id is empty, should return invalid book id error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.success(bookProgress)

        val result = runBlocking {
            bookRemote.giveUpWithBook("slnko8lk", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Give up with book - If request return 141 error, should return processing query error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.giveUpWithBook("sp8hsh908", "saoknaso8")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Give up with book - If request return 124 error, should return time out error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.giveUpWithBook("sp8hsh908", "askjba978g")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Give up with book - If request return 101 error, should return unauthorized query error`() {
        coEvery { bookApi.giveUpWithBook(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            bookRemote.giveUpWithBook("sp8hsh908", "aln98hsñpo")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(BookError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion
}
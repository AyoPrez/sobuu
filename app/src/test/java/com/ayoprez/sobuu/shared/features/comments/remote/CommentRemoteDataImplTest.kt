package com.ayoprez.sobuu.shared.features.comments.remote

import com.ayoprez.sobuu.shared.features.Utils
import com.ayoprez.sobuu.shared.models.bo_models.ReportReason
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


internal class CommentRemoteDataImplTest {

    private lateinit var commentRemote: ICommentRemoteData

    private val comment = Utils.comment
    private val report = Utils.report

    @MockK
    lateinit var commentApi: CommentApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        commentRemote = CommentRemoteDataImpl(commentApi)
    }

    //region Add comment
    @Test
    fun `Add comment - Everything good`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        assertEquals(result.data?.id, "dsoihnw98")
    }

    @Test
    fun `Add comment - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("", "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Add comment - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment(null, "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Add comment - If book id is empty, should return invalid book id error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Add comment - If text is empty, should return empty field error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.EmptyFieldError::class.java)
        )
    }

    @Test
    fun `Add comment - If page and percentage are null, should return invalid double value error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "Que pasada", null, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidDoublePageAndPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Add comment - If page and percentage have value, should return invalid double value error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "Que pasada", 3, 30, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidDoublePageAndPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Add comment - If page is negative, should return invalid page number error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "Que pasada", -3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPageNumberError::class.java)
        )
    }

    @Test
    fun `Add comment - If percentage is negative, should return invalid percentage number error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "Que pasada", null, -34, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Add comment - If percentage is more than 100, should return invalid percentage number error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "weoskn8ds",
                "Que pasada", null, 133, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Add comment - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Add comment - If request return 124 error, should return time out error`() {
        coEvery { commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Add comment - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.addComment(any(), any(), any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.addComment("9hosnd9", "0iejsn",
                "Que pasada", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Remove comment
    @Test
    fun `Remove comment - Everything good`() {
        coEvery { commentApi.removeComment(any(), any(), any()) } returns Response.success(Unit)

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "0iejsn",
                "m9s8oikd")
        }

        assertNotNull(result)
    }

    @Test
    fun `Remove comment - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.success(Unit)

        val result = runBlocking {
            commentRemote.removeComment("", "0iejsn",
                "dje98hs")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Remove comment - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.success(Unit)

        val result = runBlocking {
            commentRemote.removeComment(null, "0iejsn",
                "wdlskn98he")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Remove comment - If book id is empty, should return invalid book id error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.success(Unit)

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "",
                "ldskno9")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Remove comment - If comment id is empty, should return invalid comment id error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.success(Unit)

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "weoskn8ds",
                "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidCommentIdError::class.java)
        )
    }

    @Test
    fun `Remove comment - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "0iejsn",
                "em98ei")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Remove comment - If request return 124 error, should return time out error`() {
        coEvery { commentApi.removeComment(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "0iejsn",
                "wslmo9ew8h")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Remove comment - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.removeComment(any(), any(), any())  } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.removeComment("9hosnd9", "0iejsn",
                "edsoind9")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Edit comment
    @Test
    fun `Edit comment - Everything good`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "0iejsn", "dsm8iw",
                "Que pasada", false)
        }

        assertEquals(result.data?.id, "dsoihnw98")
    }

    @Test
    fun `Edit comment - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment("", "0iejsn", "m09ujds9",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Edit comment - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment(null, "0iejsn", "mns98yd",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Edit comment - If book id is empty, should return invalid book id error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "", "wms98jd",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Edit comment - If text is empty, should return empty field error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "weoskn8ds", "reds98hj",
                "", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.EmptyFieldError::class.java)
        )
    }

    @Test
    fun `Edit comment - If commentId is empty, should return invalid comment id error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "weoskn8ds", "",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidCommentIdError::class.java)
        )
    }

    @Test
    fun `Edit comment - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "0iejsn", "de9s8hi",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Edit comment - If request return 124 error, should return time out error`() {
        coEvery { commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "0iejsn", "dso8hiw",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Edit comment - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.editComment(any(), any(), any(), any(), any())  } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.editComment("9hosnd9", "0iejsn", "ds98hoinw",
                "Que pasada", false)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Get comments in page or percentage
    @Test
    fun `Get comments in page or percentage - Everything good`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any()) } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "0iejsn",
                null, 3)
        }

        assertEquals(result.data?.get(0)?.id, "dsoihnw98")
    }

    @Test
    fun `Get comments in page or percentage - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("", "0iejsn",
                null, 3)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage(null, "0iejsn",
                null, 3)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If book id is empty, should return invalid book id error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "",
                44, 3)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidBookIdError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If page and percentage are null, should return invalid double value error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "weoskn8ds",
                null, null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidDoublePageAndPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If page and percentage have value, should return invalid double value error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "weoskn8ds",
                3, 30)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidDoublePageAndPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If page is negative, should return invalid page number error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "weoskn8ds",
                -3, null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If percentage is negative, should return invalid percentage number error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(
            listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "weoskn8ds",
                null, -34)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If percentage is more than 100, should return invalid percentage number error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.success(listOf(comment))

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "weoskn8ds",
                null, 133)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidPercentageNumberError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "0iejsn",
                3, null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If request return 124 error, should return time out error`() {
        coEvery { commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "0iejsn",
                3, null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Get comments in page or percentage - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.getCommentsFromPageOrPercentage(any(), any(), any(), any())  } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.getCommentsInPageOrPercentage("9hosnd9", "0iejsn",
                3, null)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Increment vote counter
    @Test
    fun `Increment vote counter - Everything good`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        assertEquals(result.data?.id, "dsoihnw98")
    }

    @Test
    fun `Increment vote counter - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Increment vote counter - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter(null, "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Increment vote counter - If commentId is empty, should return invalid comment id error`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("9hosnd9", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidCommentIdError::class.java)
        )
    }

    @Test
    fun `Increment vote counter - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Increment vote counter - If request return 124 error, should return time out error`() {
        coEvery { commentApi.increaseVoteCounterComment(any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Increment vote counter - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.increaseVoteCounterComment(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.increaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Decrement vote counter
    @Test
    fun `Decrement vote counter - Everything good`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        assertEquals(result.data?.id, "dsoihnw98")
    }

    @Test
    fun `Decrement vote counter - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Decrement vote counter - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any())  } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter(null, "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Decrement vote counter - If commentId is empty, should return invalid comment id error`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any()) } returns Response.success(comment)

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("9hosnd9", "")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidCommentIdError::class.java)
        )
    }

    @Test
    fun `Decrement vote counter - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any())  } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Decrement vote counter - If request return 124 error, should return time out error`() {
        coEvery { commentApi.decreaseVoteCounterComment(any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Decrement vote counter - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.decreaseVoteCounterComment(any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.decreaseCommentVoteCounter("9hosnd9", "0iejsn")
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion

    //region Report comment
    @Test
    fun `Report comment - Everything good`() {
        coEvery { commentApi.reportComment(any(), any(), any()) } returns Response.success(report)

        val result = runBlocking {
            commentRemote.reportComment("9hosnd9", "0iejsn", ReportReason.Spam)
        }

        assertEquals(result.data?.id, "s98hioew")
    }

    @Test
    fun `Report comment - If session token is empty, should return invalid session token error`() {
        coEvery { commentApi.reportComment(any(), any(), any())  } returns Response.success(report)

        val result = runBlocking {
            commentRemote.reportComment("", "0iejsn", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Report comment - If session token is null, should return invalid session token error`() {
        coEvery { commentApi.reportComment(any(), any(), any()) } returns Response.success(report)

        val result = runBlocking {
            commentRemote.reportComment(null, "0iejsn", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidSessionTokenError::class.java)
        )
    }

    @Test
    fun `Report comment - If commentId is empty, should return invalid comment id error`() {
        coEvery { commentApi.reportComment(any(), any(), any()) } returns Response.success(report)

        val result = runBlocking {
            commentRemote.reportComment("9hosnd9", "", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.InvalidCommentIdError::class.java)
        )
    }

    @Test
    fun `Report comment - If request return 141 error, should return processing query error`() {
        coEvery { commentApi.reportComment(any(), any(), any()) } returns Response.error(404, "{\"code\":141,\"error\":\"The value being searched for must be a string.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.reportComment("9hosnd9", "0iejsn", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.ProcessingQueryError::class.java)
        )
    }

    @Test
    fun `Report comment - If request return 124 error, should return time out error`() {
        coEvery { commentApi.reportComment(any(), any(), any())  } returns Response.error(404, "{\"code\":124,\"error\":\"Timeout.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.reportComment("9hosnd9", "0iejsn", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.TimeOutError::class.java)
        )
    }

    @Test
    fun `Report comment - If request return 101 error, should return unauthorized query error`() {
        coEvery {commentApi.reportComment(any(), any(), any()) } returns Response.error(404, "{\"code\":101,\"error\":\"Object not found.\"}".toResponseBody())

        val result = runBlocking {
            commentRemote.reportComment("9hosnd9", "0iejsn", ReportReason.Spam)
        }

        MatcherAssert.assertThat(
            result.error,
            CoreMatchers.instanceOf(CommentError.UnauthorizedQueryError::class.java)
        )
    }
    //endregion
}
package com.ayoprez.sobuu.shared.features.comments.repository

import com.ayoprez.sobuu.shared.features.comments.database.ICommentLocalData
import com.ayoprez.sobuu.shared.features.comments.remote.CommentError
import com.ayoprez.sobuu.shared.features.comments.remote.CommentResult
import com.ayoprez.sobuu.shared.features.comments.remote.ICommentRemoteData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test

internal class CommentRepositoryImplTest {
    private lateinit var commentRepo: ICommentRepository

    @MockK
    lateinit var remoteData: ICommentRemoteData

    @MockK
    lateinit var localData: ICommentLocalData

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        commentRepo = CommentRepositoryImpl(commentRemoteData = remoteData, commentLocalData = localData)
    }

    //region Add comment
    @Test
    fun `Add comment - Everything good`() {
        coEvery { remoteData.addComment(any(), any(), any(), any(), any(), any(), any()) } returns CommentResult.Success()
        coEvery { localData.getSessionToken() } returns "TestToken"

        val result = runBlocking {
            commentRepo.addComment("we98hd", "Comment test", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(CommentResult.Success::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Add comment - Empty session token, should return error`() {
        coEvery { remoteData.addComment(any(), any(), any(), any(), any(), any(), any()) } returns CommentResult.Error(error = CommentError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            commentRepo.addComment("we98hd", "Comment test", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(CommentResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Add comment - Null session token, should return error`() {
        coEvery { remoteData.addComment(any(), any(), any(), any(), any(), any(), any()) } returns CommentResult.Error(error = CommentError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            commentRepo.addComment("we98hd", "Comment test", 3, null, null, false)
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(CommentResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }
    //endregion

}
package com.ayoprez.sobuu.shared.features.book.repository

import com.ayoprez.sobuu.shared.features.book.database.IBookLocalData
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.features.book.remote.BookResult
import com.ayoprez.sobuu.shared.features.book.remote.IBookRemoteData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test

internal class BookRepositoryImplTest {

    private lateinit var bookRepo: IBookRepository

    @MockK
    lateinit var remoteData: IBookRemoteData

    @MockK
    lateinit var localData: IBookLocalData

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        bookRepo = BookRepositoryImpl(bookRemoteData = remoteData, bookLocalData = localData)
    }

    //region Get User Current Reading Book
    @Test
    fun `Get User Current Reading Book - Everything good`() {
        coEvery { remoteData.getUserCurrentReadingBook(any()) } returns BookResult.Success(listOf())
        coEvery { localData.getSessionToken() } returns "TestToken"

        val result = runBlocking {
            bookRepo.getUserCurrentReadingBook()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(BookResult.Success::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Get User Current Reading Book - Empty session token, should return error`() {
        coEvery { remoteData.getUserCurrentReadingBook(any()) } returns BookResult.Error(error = BookError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns ""

        val result = runBlocking {
            bookRepo.getUserCurrentReadingBook()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(BookResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }

    @Test
    fun `Get User Current Reading Book - Null session token, should return error`() {
        coEvery { remoteData.getUserCurrentReadingBook(any()) } returns BookResult.Error(error = BookError.InvalidSessionTokenError)
        coEvery { localData.getSessionToken() } returns null

        val result = runBlocking {
            bookRepo.getUserCurrentReadingBook()
        }

        MatcherAssert.assertThat(
            result, CoreMatchers.instanceOf(BookResult.Error::class.java)
        )
        coVerify { localData.getSessionToken() }
    }
    //endregion

}
package com.ayoprez.sobuu.shared.features.shelf.remote

import com.ayoprez.sobuu.shared.models.Shelf
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ShelfRemoteDataImpl @Inject constructor(
    private val api: ShelfApi
): IShelfRemoteData {

    override suspend fun searchShelvesFromApi(sessionToken: String?, term: String): ShelfResult<List<Shelf>> {
        if (term.isBlank()) return ShelfResult.Error(ShelfError.EmptyTerm)
        return execute(sessionToken) {
            api.searchShelf(it, term)
        }
    }

    override suspend fun getAllUserShelves(sessionToken: String?): ShelfResult<List<Shelf>> {
        return execute(sessionToken) {
            api.getAllUserShelves(it)
        }
    }

    override suspend fun createShelf(
        sessionToken: String?,
        name: String,
        description: String,
        isPublic: Boolean
    ): ShelfResult<Shelf> {
        if (name.isBlank()) return ShelfResult.Error(ShelfError.EmptyName)
        if (description.isBlank()) return ShelfResult.Error(ShelfError.EmptyDescription)
        return execute(sessionToken) {
            api.createShelf(
                sessionToken = it,
                name = name,
                description = description,
                isPublic = isPublic,
            )
        }
    }

    override suspend fun changeShelfName(
        sessionToken: String?,
        shelfId: String,
        newName: String
    ): ShelfResult<Shelf> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        if (newName.isBlank()) return ShelfResult.Error(ShelfError.EmptyName)
        return execute(sessionToken) {
            api.changeShelfName(
                sessionToken = it,
                shelfId = shelfId,
                newName = newName
            )
        }
    }

    override suspend fun changeShelfDescription(
        sessionToken: String?,
        shelfId: String,
        newDescription: String
    ): ShelfResult<Shelf> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        if (newDescription.isBlank()) return ShelfResult.Error(ShelfError.EmptyDescription)
        return execute(sessionToken) {
            api.changeShelfDescription(
                sessionToken = it,
                shelfId = shelfId,
                newDescription = newDescription,
            )
        }
    }

    override suspend fun changeShelfPrivacy(
        sessionToken: String?,
        shelfId: String,
        isPublic: Boolean
    ): ShelfResult<Shelf> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        return execute(sessionToken) {
            api.changeShelfPrivacy(
                sessionToken = it,
                shelfId = shelfId,
                isPublic = isPublic,
            )
        }
    }

    override suspend fun addBookToShelf(
        sessionToken: String?,
        shelfId: String,
        bookId: String
    ): ShelfResult<Shelf> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        if (bookId.isBlank()) return ShelfResult.Error(ShelfError.EmptyBookId)
        return execute(sessionToken) {
            api.addBookToShelf(
                sessionToken = it,
                shelfId = shelfId,
                bookId = bookId
            )
        }
    }

    override suspend fun removeBookFromShelf(
        sessionToken: String?,
        shelfId: String,
        bookId: String
    ): ShelfResult<Shelf> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        if (bookId.isBlank()) return ShelfResult.Error(ShelfError.EmptyBookId)
        return execute(sessionToken) {
            api.removeBookFromShelf(
                sessionToken = it,
                shelfId = shelfId,
                bookId = bookId
            )
        }
    }

    override suspend fun removeShelf(
        sessionToken: String?,
        shelfId: String
    ): ShelfResult<Unit> {
        if (shelfId.isBlank()) return ShelfResult.Error(ShelfError.EmptyShelfId)
        return execute(sessionToken) {
            api.removeShelf(
                sessionToken = it,
                shelfId = shelfId
            )
        }
    }

    private suspend fun <T>execute(sessionToken: String?, func: suspend (sessionToken: String) -> Response<T>): ShelfResult<T> {
        return try {
            if (sessionToken.isNullOrBlank()) return ShelfResult.Error(ShelfError.InvalidSessionTokenError)

            val result = func(sessionToken)

            if (result.body() == null && result.errorBody() != null) return handleResponseError(
                result.errorBody()
            )

            return ShelfResult.Success(data = result.body())
        } catch(e: HttpException) {
            when(e.code()) {
                401 -> ShelfResult.Error(ShelfError.UnauthorizedQueryError)
                209 -> ShelfResult.Error(ShelfError.InvalidSessionTokenError)
                else -> ShelfResult.Error(ShelfError.UnknownError)
            }
        } catch (e: Exception) {
            ShelfResult.Error(ShelfError.UnknownError)
        }
    }

    private fun <T>handleResponseError(errorBody: ResponseBody?): ShelfResult<T> {
        if (errorBody == null) return ShelfResult.Error(error = ShelfError.UnknownError)
        val response = errorBody.string()

        return JSONObject(response)
            .get("code")
            .let {
                when(it) {
                    101 -> ShelfResult.Error(ShelfError.UnauthorizedQueryError)
                    141 -> ShelfResult.Error(ShelfError.ProcessingQueryError)
                    124 -> ShelfResult.Error(ShelfError.TimeOutError)
                    209 -> ShelfResult.Error(ShelfError.InvalidSessionTokenError)
                    else -> ShelfResult.Error(ShelfError.UnknownError)
                }
            }
    }
}
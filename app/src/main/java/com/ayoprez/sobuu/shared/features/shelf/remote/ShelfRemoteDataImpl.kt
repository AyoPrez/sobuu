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
        //TODO Commit and Push this before remove this code
//        return try {
//            if (sessionToken.isNullOrBlank()) return ShelfResult.Error(ShelfError.InvalidSessionTokenError)
//            if (term.isBlank()) return ShelfResult.Error(ShelfError.EmptyTerm)
//
//            val result = api.searchShelf(sessionToken, term)
//
//            if (result.body() == null && result.errorBody() != null) return handleResponseError(
//                result.errorBody()
//            )
//
//            return ShelfResult.Success(data = result.body())
//        } catch(e: HttpException) {
//            when(e.code()) {
//                401 -> ShelfResult.Error(ShelfError.UnauthorizedQueryError)
//                209 -> ShelfResult.Error(ShelfError.InvalidSessionTokenError)
//                else -> ShelfResult.Error(ShelfError.UnknownError)
//            }
//        } catch (e: Exception) {
//            ShelfResult.Error(ShelfError.UnknownError)
//        }
    }

    override suspend fun getAllUserShelves(sessionToken: String?): ShelfResult<List<Shelf>> {
        return execute(sessionToken) {
            api.getAllUserShelves(it)
        }
//        return try {
//            if (sessionToken.isNullOrBlank()) return ShelfResult.Error(ShelfError.InvalidSessionTokenError)
//
//            val result = api.getAllUserShelves(sessionToken)
//
//            if (result.body() == null && result.errorBody() != null) return handleResponseError(
//                result.errorBody()
//            )
//
//            return ShelfResult.Success(data = result.body())
//        } catch(e: HttpException) {
//            when(e.code()) {
//                401 -> ShelfResult.Error(ShelfError.UnauthorizedQueryError)
//                209 -> ShelfResult.Error(ShelfError.InvalidSessionTokenError)
//                else -> ShelfResult.Error(ShelfError.UnknownError)
//            }
//        } catch (e: Exception) {
//            ShelfResult.Error(ShelfError.UnknownError)
//        }
    }

    private suspend fun execute(sessionToken: String?, func: suspend (sessionToken: String) -> Response<List<Shelf>>): ShelfResult<List<Shelf>> {
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
package com.ayoprez.sobuu.shared.features.shelf.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.authentication.database.AuthenticationLocalDataImpl.Constants.SESSION_TOKEN_KEY
import javax.inject.Inject

class ShelfLocalDataImpl @Inject constructor(
    private val prefs: SharedPreferences
): IShelfLocalData {

    object Constants {
        const val SESSION_TOKEN_KEY = "authToken"
    }

    override suspend fun getSessionToken(): String? {
        return prefs.getString(SESSION_TOKEN_KEY, null)
    }
}
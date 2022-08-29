package com.ayoprez.sobuu.shared.features.authentication.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.features.authentication.database.AuthenticationLocalDataImpl.Constants.SESSION_TOKEN_KEY
import javax.inject.Inject

class AuthenticationLocalDataImpl @Inject constructor(
    private val prefs: SharedPreferences
): IAuthenticationLocalData {

    object Constants {
        const val SESSION_TOKEN_KEY = "authToken"
    }

    override suspend fun getSessionToken(): String? {
        return prefs.getString(SESSION_TOKEN_KEY, null)
    }

    override suspend fun setSessionToken(token: String?) {
        prefs.edit()
            .putString(SESSION_TOKEN_KEY, token)
            .apply()
    }
}
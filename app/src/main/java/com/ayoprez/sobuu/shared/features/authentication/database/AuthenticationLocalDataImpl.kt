package com.ayoprez.sobuu.shared.features.authentication.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.core.SessionTokenManager
import com.ayoprez.sobuu.shared.core.SessionTokenManager.Constants.SESSION_TOKEN_KEY
import javax.inject.Inject

class AuthenticationLocalDataImpl @Inject constructor(
    private val prefs: SharedPreferences
): IAuthenticationLocalData, SessionTokenManager(prefs) {

    override suspend fun getSessionToken(): String? {
        return super.obtainSessionToken()
    }

    override suspend fun setSessionToken(token: String?) {
        prefs.edit()
            .putString(SESSION_TOKEN_KEY, token)
            .apply()
    }
}
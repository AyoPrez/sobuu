package com.ayoprez.sobuu.shared.core

import android.content.SharedPreferences

open class SessionTokenManager constructor(private val prefs: SharedPreferences) {

    object Constants {
        const val SESSION_TOKEN_KEY = "authToken"
    }

    fun obtainSessionToken(): String? {
        return prefs.getString(Constants.SESSION_TOKEN_KEY, null)
    }
}
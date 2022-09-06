package com.ayoprez.sobuu.shared.features.book.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.core.SessionTokenManager
import javax.inject.Inject

class BookLocalDataImpl @Inject constructor(
    prefs: SharedPreferences
): IBookLocalData, SessionTokenManager(prefs) {

    override suspend fun getSessionToken(): String? {
        return super.obtainSessionToken()
    }
}
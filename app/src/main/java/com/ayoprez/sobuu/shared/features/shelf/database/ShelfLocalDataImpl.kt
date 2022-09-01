package com.ayoprez.sobuu.shared.features.shelf.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.core.SessionTokenManager
import javax.inject.Inject

class ShelfLocalDataImpl @Inject constructor(
    prefs: SharedPreferences
): IShelfLocalData, SessionTokenManager(prefs) {

    override suspend fun getSessionToken(): String? {
        return super.obtainSessionToken()
    }
}